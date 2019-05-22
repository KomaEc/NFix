package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.If;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class DeadCodeEliminator extends DataFlow {
   public DeadCodeEliminator(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf, false, true);
   }

   public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
      return new DeadCodeEliminator.DataFlowItem();
   }

   public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
      DeadCodeEliminator.DataFlowItem result = null;
      Iterator it = inItems.iterator();

      while(it.hasNext()) {
         DeadCodeEliminator.DataFlowItem inItem = (DeadCodeEliminator.DataFlowItem)it.next();
         if (result == null) {
            result = new DeadCodeEliminator.DataFlowItem(inItem);
         } else {
            result.union(inItem);
         }
      }

      return result;
   }

   public Map flow(DataFlow.Item in, FlowGraph graph, Term t, Set succEdgeKeys) {
      return itemToMap(this.flow(in, graph, t), succEdgeKeys);
   }

   protected DeadCodeEliminator.DataFlowItem flow(DataFlow.Item in, FlowGraph graph, Term t) {
      DeadCodeEliminator.DataFlowItem result = new DeadCodeEliminator.DataFlowItem((DeadCodeEliminator.DataFlowItem)in);
      Set[] du = null;
      if (t instanceof LocalDecl) {
         LocalDecl n = (LocalDecl)t;
         LocalInstance to = n.localInstance();
         result.removeDecl(to);
         du = this.getDefUse(n.init());
      } else if (t instanceof Stmt && !(t instanceof CompoundStmt)) {
         du = this.getDefUse((Stmt)t);
      } else if (t instanceof CompoundStmt) {
         if (t instanceof If) {
            du = this.getDefUse(((If)t).cond());
         } else if (t instanceof Switch) {
            du = this.getDefUse(((Switch)t).expr());
         } else if (t instanceof Do) {
            du = this.getDefUse(((Do)t).cond());
         } else if (t instanceof For) {
            du = this.getDefUse(((For)t).cond());
         } else if (t instanceof While) {
            du = this.getDefUse(((While)t).cond());
         }
      }

      if (du != null) {
         result.removeAll(du[0]);
         result.addAll(du[1]);
      }

      return result;
   }

   public void post(FlowGraph graph, Term root) throws SemanticException {
      if (Report.should_report((String)"cfg", 2)) {
         this.dumpFlowGraph(graph, root);
      }

   }

   public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
      throw new InternalCompilerError("DeadCodeEliminator.check should never be called.");
   }

   private DeadCodeEliminator.DataFlowItem getItem(Term n) {
      FlowGraph g = this.currentFlowGraph();
      if (g == null) {
         return null;
      } else {
         Collection peers = g.peers(n);
         if (peers != null && !peers.isEmpty()) {
            List items = new ArrayList();
            Iterator it = peers.iterator();

            while(it.hasNext()) {
               FlowGraph.Peer p = (FlowGraph.Peer)it.next();
               if (p.inItem() != null) {
                  items.add(p.inItem());
               }
            }

            return (DeadCodeEliminator.DataFlowItem)this.confluence(items, n, g);
         } else {
            return null;
         }
      }
   }

   public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      if (n instanceof LocalDecl) {
         LocalDecl ld = (LocalDecl)n;
         DeadCodeEliminator.DataFlowItem in = this.getItem(ld);
         return (Node)(in != null && !in.needDecl(ld.localInstance()) ? this.getEffects(ld.init()) : n);
      } else if (n instanceof Eval) {
         Eval eval = (Eval)n;
         Expr expr = eval.expr();
         Expr right = null;
         Local var14;
         if (expr instanceof Assign) {
            Assign assign = (Assign)expr;
            Expr left = assign.left();
            right = assign.right();
            if (!(left instanceof Local)) {
               return n;
            }

            var14 = (Local)left;
         } else {
            if (!(expr instanceof Unary)) {
               return n;
            }

            Unary unary = (Unary)expr;
            expr = unary.expr();
            if (!(expr instanceof Local)) {
               return n;
            }

            var14 = (Local)expr;
         }

         DeadCodeEliminator.DataFlowItem in = this.getItem(eval);
         if (in != null && !in.needDef(var14.localInstance())) {
            return (Node)(right != null ? this.getEffects(right) : this.nf.Empty(Position.COMPILER_GENERATED));
         } else {
            return n;
         }
      } else if (n instanceof Block) {
         Block b = (Block)n;
         List stmts = new ArrayList(b.statements());
         Iterator it = stmts.iterator();

         while(it.hasNext()) {
            if (it.next() instanceof Empty) {
               it.remove();
            }
         }

         return b.statements(stmts);
      } else {
         return n;
      }
   }

   protected Set[] getDefUse(Node n) {
      Set def = new HashSet();
      Set use = new HashSet();
      if (n != null) {
         n.visit(this.createDefUseFinder(def, use));
      }

      return new Set[]{def, use};
   }

   protected NodeVisitor createDefUseFinder(Set def, Set use) {
      return new DeadCodeEliminator.DefUseFinder(def, use);
   }

   protected Stmt getEffects(Expr expr) {
      Stmt empty = this.nf.Empty(Position.COMPILER_GENERATED);
      if (expr == null) {
         return empty;
      } else {
         final List result = new LinkedList();
         final Position pos = Position.COMPILER_GENERATED;
         NodeVisitor v = new HaltingVisitor() {
            public NodeVisitor enter(Node n) {
               if (!(n instanceof Assign) && !(n instanceof ProcedureCall)) {
                  if (n instanceof Unary) {
                     Unary.Operator op = ((Unary)n).operator();
                     if (op == Unary.POST_INC || op == Unary.POST_DEC || op == Unary.PRE_INC || op == Unary.PRE_INC) {
                        return this.bypassChildren(n);
                     }
                  }

                  return this;
               } else {
                  return this.bypassChildren(n);
               }
            }

            public Node leave(Node old, Node n, NodeVisitor v) {
               if (!(n instanceof Assign) && !(n instanceof ProcedureCall)) {
                  if (n instanceof Unary) {
                     Unary.Operator op = ((Unary)n).operator();
                     if (op == Unary.POST_INC || op == Unary.POST_DEC || op == Unary.PRE_INC || op == Unary.PRE_INC) {
                        result.add(DeadCodeEliminator.this.nf.Eval(pos, (Expr)n));
                     }
                  }
               } else {
                  result.add(DeadCodeEliminator.this.nf.Eval(pos, (Expr)n));
               }

               return n;
            }
         };
         expr.visit(v);
         if (result.isEmpty()) {
            return empty;
         } else {
            return (Stmt)(result.size() == 1 ? (Stmt)result.get(0) : this.nf.Block(Position.COMPILER_GENERATED, (List)result));
         }
      }
   }

   protected static class DefUseFinder extends HaltingVisitor {
      protected Set def;
      protected Set use;

      public DefUseFinder(Set def, Set use) {
         this.def = def;
         this.use = use;
      }

      public NodeVisitor enter(Node n) {
         return (NodeVisitor)(n instanceof LocalAssign ? this.bypass(((Assign)n).left()) : super.enter(n));
      }

      public Node leave(Node old, Node n, NodeVisitor v) {
         if (n instanceof Local) {
            this.use.add(((Local)n).localInstance());
         } else if (n instanceof Assign) {
            Expr left = ((Assign)n).left();
            if (left instanceof Local) {
               this.def.add(((Local)left).localInstance());
            }
         }

         return n;
      }
   }

   protected static class DataFlowItem extends DataFlow.Item {
      private Set liveVars;
      private Set liveDecls;

      protected DataFlowItem() {
         this.liveVars = new HashSet();
         this.liveDecls = new HashSet();
      }

      protected DataFlowItem(DeadCodeEliminator.DataFlowItem dfi) {
         this.liveVars = new HashSet(dfi.liveVars);
         this.liveDecls = new HashSet(dfi.liveDecls);
      }

      public void add(LocalInstance li) {
         this.liveVars.add(li);
         this.liveDecls.add(li);
      }

      public void addAll(Set lis) {
         this.liveVars.addAll(lis);
         this.liveDecls.addAll(lis);
      }

      public void remove(LocalInstance li) {
         this.liveVars.remove(li);
      }

      public void removeAll(Set lis) {
         this.liveVars.removeAll(lis);
      }

      public void removeDecl(LocalInstance li) {
         this.liveVars.remove(li);
         this.liveDecls.remove(li);
      }

      public void union(DeadCodeEliminator.DataFlowItem dfi) {
         this.liveVars.addAll(dfi.liveVars);
         this.liveDecls.addAll(dfi.liveDecls);
      }

      protected boolean needDecl(LocalInstance li) {
         return this.liveDecls.contains(li);
      }

      protected boolean needDef(LocalInstance li) {
         return this.liveVars.contains(li);
      }

      public int hashCode() {
         int result = 0;

         Iterator it;
         for(it = this.liveVars.iterator(); it.hasNext(); result = 31 * result + it.next().hashCode()) {
         }

         for(it = this.liveDecls.iterator(); it.hasNext(); result = 31 * result + it.next().hashCode()) {
         }

         return result;
      }

      public boolean equals(Object o) {
         if (!(o instanceof DeadCodeEliminator.DataFlowItem)) {
            return false;
         } else {
            DeadCodeEliminator.DataFlowItem dfi = (DeadCodeEliminator.DataFlowItem)o;
            return this.liveVars.equals(dfi.liveVars) && this.liveDecls.equals(dfi.liveDecls);
         }
      }

      public String toString() {
         return "<vars=" + this.liveVars + " ; decls=" + this.liveDecls + ">";
      }
   }
}
