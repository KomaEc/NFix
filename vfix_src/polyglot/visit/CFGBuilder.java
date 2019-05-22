package polyglot.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Catch;
import polyglot.ast.CodeDecl;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Labeled;
import polyglot.ast.Loop;
import polyglot.ast.Return;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Term;
import polyglot.ast.Try;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;

public class CFGBuilder implements Copy {
   protected FlowGraph graph;
   protected TypeSystem ts;
   protected CFGBuilder outer;
   protected Stmt innermostTarget;
   protected List path_to_finally;
   protected DataFlow df;
   protected boolean skipInnermostCatches;
   protected boolean errorEdgesToExitNode;
   static int counter = 0;

   public CFGBuilder(TypeSystem ts, FlowGraph graph, DataFlow df) {
      this.ts = ts;
      this.graph = graph;
      this.df = df;
      this.path_to_finally = Collections.EMPTY_LIST;
      this.outer = null;
      this.innermostTarget = null;
      this.skipInnermostCatches = false;
      this.errorEdgesToExitNode = false;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public Object copy() {
      try {
         return (CFGBuilder)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   public CFGBuilder push(Stmt n) {
      return this.push(n, false);
   }

   public CFGBuilder push(Stmt n, boolean skipInnermostCatches) {
      CFGBuilder v = (CFGBuilder)this.copy();
      v.outer = this;
      v.innermostTarget = n;
      v.skipInnermostCatches = skipInnermostCatches;
      return v;
   }

   public void visitBranchTarget(Branch b) {
      Term last = b;
      CFGBuilder last_visitor = this;

      for(CFGBuilder v = this; v != null; v = v.outer) {
         Term c = v.innermostTarget;
         if (c instanceof Try) {
            Try tr = (Try)c;
            if (tr.finallyBlock() != null) {
               last_visitor = tryFinally(v, (Term)last, last_visitor, tr.finallyBlock());
               last = tr.finallyBlock();
            }
         }

         if (b.label() != null) {
            if (c instanceof Labeled) {
               Labeled l = (Labeled)c;
               if (l.label().equals(b.label())) {
                  if (b.kind() == Branch.BREAK) {
                     this.edge(last_visitor, (Term)last, l, FlowGraph.EDGE_KEY_OTHER);
                  } else {
                     Stmt s = l.statement();
                     if (!(s instanceof Loop)) {
                        throw new CFGBuildError("Target of continue statement must be a loop.", l.position());
                     }

                     Loop loop = (Loop)s;
                     this.edge(last_visitor, (Term)last, loop.continueTarget(), FlowGraph.EDGE_KEY_OTHER);
                  }

                  return;
               }
            }
         } else {
            if (c instanceof Loop) {
               Loop l = (Loop)c;
               if (b.kind() == Branch.CONTINUE) {
                  this.edge(last_visitor, (Term)last, l.continueTarget(), FlowGraph.EDGE_KEY_OTHER);
               } else {
                  this.edge(last_visitor, (Term)last, l, FlowGraph.EDGE_KEY_OTHER);
               }

               return;
            }

            if (c instanceof Switch && b.kind() == Branch.BREAK) {
               this.edge(last_visitor, (Term)last, c, FlowGraph.EDGE_KEY_OTHER);
               return;
            }
         }
      }

      throw new CFGBuildError("Target of branch statement not found.", b.position());
   }

   public void visitReturn(Return r) {
      Term last = r;
      CFGBuilder last_visitor = this;

      for(CFGBuilder v = this; v != null; v = v.outer) {
         Term c = v.innermostTarget;
         if (c instanceof Try) {
            Try tr = (Try)c;
            if (tr.finallyBlock() != null) {
               last_visitor = tryFinally(v, (Term)last, last_visitor, tr.finallyBlock());
               last = tr.finallyBlock();
            }
         }
      }

      this.edge(last_visitor, (Term)last, this.graph.exitNode(), FlowGraph.EDGE_KEY_OTHER);
   }

   public void visitGraph() {
      String name = StringUtil.getShortNameComponent(this.df.getClass().getName());
      name = name + counter++;
      if (Report.should_report((String)"cfg", 2)) {
         String rootName = "";
         if (this.graph.root() instanceof CodeDecl) {
            CodeDecl cd = (CodeDecl)this.graph.root();
            rootName = cd.codeInstance().toString() + " in " + cd.codeInstance().container().toString();
         }

         Report.report(2, "digraph CFGBuild" + name + " {");
         Report.report(2, "  label=\"CFGBuilder: " + name + "\\n" + rootName + "\"; fontsize=20; center=true; ratio=auto; size = \"8.5,11\";");
      }

      this.graph.peer(this.graph.entryNode(), Collections.EMPTY_LIST, this.df);
      this.graph.peer(this.graph.exitNode(), Collections.EMPTY_LIST, this.df);
      this.visitCFG(this.graph.root(), Collections.EMPTY_LIST);
      if (Report.should_report((String)"cfg", 2)) {
         Report.report(2, "}");
      }

   }

   public void visitCFGList(List elements, Term after) {
      Term prev = null;

      Term c;
      for(Iterator i = elements.iterator(); i.hasNext(); prev = c) {
         c = (Term)i.next();
         if (prev != null) {
            this.visitCFG(prev, c.entry());
         }
      }

      if (prev != null) {
         this.visitCFG(prev, after);
      }

   }

   public void visitCFG(Term a, Term succ) {
      this.visitCFG(a, FlowGraph.EDGE_KEY_OTHER, succ);
   }

   public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey, Term succ) {
      this.visitCFG(a, CollectionUtil.list(new CFGBuilder.EdgeKeyTermPair(edgeKey, succ)));
   }

   public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey1, Term succ1, FlowGraph.EdgeKey edgeKey2, Term succ2) {
      this.visitCFG(a, CollectionUtil.list(new CFGBuilder.EdgeKeyTermPair(edgeKey1, succ1), new CFGBuilder.EdgeKeyTermPair(edgeKey2, succ2)));
   }

   public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey, List succ) {
      List l = new ArrayList(2 * succ.size());
      Iterator iter = succ.iterator();

      while(iter.hasNext()) {
         l.add(new CFGBuilder.EdgeKeyTermPair(edgeKey, (Term)iter.next()));
      }

      this.visitCFG(a, (List)l);
   }

   protected void visitCFG(Term a, List succs) {
      if (Report.should_report((String)"cfg", 2)) {
         Report.report(2, "// node " + a + " -> " + succs);
      }

      succs = a.acceptCFG(this, succs);
      Iterator i = succs.iterator();

      while(i.hasNext()) {
         CFGBuilder.EdgeKeyTermPair pair = (CFGBuilder.EdgeKeyTermPair)i.next();
         this.edge(a, pair.term, pair.edgeKey);
      }

      this.visitThrow(a);
   }

   public void visitThrow(Term a) {
      Iterator i = a.del().throwTypes(this.ts).iterator();

      while(i.hasNext()) {
         Type type = (Type)i.next();
         this.visitThrow(a, type);
      }

      if (a instanceof Stmt && !(a instanceof CompoundStmt) || a instanceof Block && ((Block)a).statements().isEmpty()) {
         this.visitThrow(a, this.ts.Error());
      }

   }

   public void visitThrow(Term t, Type type) {
      Term last = t;
      CFGBuilder last_visitor = this;

      for(CFGBuilder v = this; v != null; v = v.outer) {
         Term c = v.innermostTarget;
         if (c instanceof Try) {
            Try tr = (Try)c;
            if (!v.skipInnermostCatches) {
               boolean definiteCatch = false;
               Iterator i = tr.catchBlocks().iterator();

               while(i.hasNext()) {
                  Catch cb = (Catch)i.next();
                  if (type.isImplicitCastValid(cb.catchType())) {
                     this.edge(last_visitor, (Term)last, cb.entry(), new FlowGraph.ExceptionEdgeKey(type));
                     definiteCatch = true;
                  } else if (cb.catchType().isImplicitCastValid(type)) {
                     this.edge(last_visitor, (Term)last, cb.entry(), new FlowGraph.ExceptionEdgeKey(cb.catchType()));
                  }
               }

               if (definiteCatch) {
                  return;
               }
            }

            if (tr.finallyBlock() != null) {
               last_visitor = tryFinally(v, (Term)last, last_visitor, tr.finallyBlock());
               last = tr.finallyBlock();
            }
         }
      }

      if (this.errorEdgesToExitNode || !type.isSubtype(this.ts.Error())) {
         this.edge(last_visitor, (Term)last, this.graph.exitNode(), new FlowGraph.ExceptionEdgeKey(type));
      }

   }

   protected static CFGBuilder tryFinally(CFGBuilder v, Term last, CFGBuilder last_visitor, Block finallyBlock) {
      CFGBuilder v_ = v.outer.enterFinally(last);
      v_.edge(last_visitor, last, finallyBlock.entry(), FlowGraph.EDGE_KEY_OTHER);
      v_.visitCFG(finallyBlock, (List)Collections.EMPTY_LIST);
      return v_;
   }

   protected CFGBuilder enterFinally(Term from) {
      CFGBuilder v = (CFGBuilder)this.copy();
      v.path_to_finally = new ArrayList(this.path_to_finally.size() + 1);
      v.path_to_finally.addAll(this.path_to_finally);
      v.path_to_finally.add(from);
      return v;
   }

   public void edge(Term p, Term q) {
      this.edge(this, p, q, FlowGraph.EDGE_KEY_OTHER);
   }

   public void edge(Term p, Term q, FlowGraph.EdgeKey edgeKey) {
      this.edge(this, p, q, edgeKey);
   }

   public void edge(CFGBuilder p_visitor, Term p, Term q, FlowGraph.EdgeKey edgeKey) {
      if (Report.should_report((String)"cfg", 2)) {
         Report.report(2, "//     edge " + p + " -> " + q);
      }

      FlowGraph.Peer pp = this.graph.peer(p, p_visitor.path_to_finally, this.df);
      FlowGraph.Peer pq = this.graph.peer(q, this.path_to_finally, this.df);
      if (Report.should_report((String)"cfg", 3)) {
         Report.report(2, pp.hashCode() + " [ label = \"" + StringUtil.escape(pp.toString()) + "\" ];");
         Report.report(2, pq.hashCode() + " [ label = \"" + StringUtil.escape(pq.toString()) + "\" ];");
      } else if (Report.should_report((String)"cfg", 2)) {
         Report.report(2, pp.hashCode() + " [ label = \"" + StringUtil.escape(pp.node.toString()) + "\" ];");
         Report.report(2, pq.hashCode() + " [ label = \"" + StringUtil.escape(pq.node.toString()) + "\" ];");
      }

      if (this.graph.forward()) {
         if (Report.should_report((String)"cfg", 2)) {
            Report.report(2, pp.hashCode() + " -> " + pq.hashCode() + " [label=\"" + edgeKey + "\"];");
         }

         pp.succs.add(new FlowGraph.Edge(edgeKey, pq));
         pq.preds.add(new FlowGraph.Edge(edgeKey, pp));
      } else {
         if (Report.should_report((String)"cfg", 2)) {
            Report.report(2, pq.hashCode() + " -> " + pp.hashCode() + " [label=\"" + edgeKey + "\"];");
         }

         pq.succs.add(new FlowGraph.Edge(edgeKey, pp));
         pp.preds.add(new FlowGraph.Edge(edgeKey, pq));
      }

   }

   protected static class EdgeKeyTermPair {
      FlowGraph.EdgeKey edgeKey;
      Term term;

      public EdgeKeyTermPair(FlowGraph.EdgeKey edgeKey, Term term) {
         this.edgeKey = edgeKey;
         this.term = term;
      }

      public String toString() {
         return "{edgeKey=" + this.edgeKey + ",term=" + this.term + "}";
      }
   }
}
