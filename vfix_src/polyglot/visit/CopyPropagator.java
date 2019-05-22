package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.If;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class CopyPropagator extends DataFlow {
   public CopyPropagator(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf, true, true);
   }

   public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
      return new CopyPropagator.DataFlowItem();
   }

   public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
      CopyPropagator.DataFlowItem result = null;
      Iterator it = inItems.iterator();

      while(it.hasNext()) {
         CopyPropagator.DataFlowItem inItem = (CopyPropagator.DataFlowItem)it.next();
         if (result == null) {
            result = new CopyPropagator.DataFlowItem(inItem);
         } else {
            result.intersect(inItem);
         }
      }

      return result;
   }

   private void killDecl(CopyPropagator.DataFlowItem dfi, Stmt stmt) {
      if (stmt instanceof LocalDecl) {
         dfi.kill(((LocalDecl)stmt).localInstance());
      }

   }

   protected CopyPropagator.DataFlowItem flow(DataFlow.Item in, FlowGraph graph, Term t) {
      CopyPropagator.DataFlowItem result = new CopyPropagator.DataFlowItem((CopyPropagator.DataFlowItem)in);
      Expr expr;
      if (t instanceof Assign) {
         Assign n = (Assign)t;
         Assign.Operator op = n.operator();
         expr = n.left();
         Expr right = n.right();
         if (expr instanceof Local) {
            LocalInstance to = ((Local)expr).localInstance();
            result.kill(to);
            if (right instanceof Local && op == Assign.ASSIGN) {
               LocalInstance from = ((Local)right).localInstance();
               result.add(from, to);
            }
         }
      } else if (t instanceof Unary) {
         Unary n = (Unary)t;
         Unary.Operator op = n.operator();
         expr = n.expr();
         if (expr instanceof Local && (op == Unary.POST_INC || op == Unary.POST_DEC || op == Unary.PRE_INC || op == Unary.PRE_DEC)) {
            result.kill(((Local)expr).localInstance());
         }
      } else if (t instanceof LocalDecl) {
         LocalDecl n = (LocalDecl)t;
         LocalInstance to = n.localInstance();
         result.kill(to);
         if (!n.flags().isFinal() && n.init() instanceof Local) {
            LocalInstance from = ((Local)n.init()).localInstance();
            result.add(from, to);
         }
      } else {
         Iterator it;
         if (t instanceof Block) {
            Block n = (Block)t;
            it = n.statements().iterator();

            while(it.hasNext()) {
               this.killDecl(result, (Stmt)it.next());
            }
         } else if (t instanceof Loop) {
            if (t instanceof For) {
               For n = (For)t;
               it = n.inits().iterator();

               while(it.hasNext()) {
                  this.killDecl(result, (Stmt)it.next());
               }
            }

            this.killDecl(result, ((Loop)t).body());
         } else if (t instanceof Catch) {
            result.kill(((Catch)t).formal().localInstance());
         } else if (t instanceof If) {
            If n = (If)t;
            this.killDecl(result, n.consequent());
            this.killDecl(result, n.alternative());
         }
      }

      return result;
   }

   public Map flow(DataFlow.Item in, FlowGraph graph, Term t, Set succEdgeKeys) {
      return itemToMap(this.flow(in, graph, t), succEdgeKeys);
   }

   public void post(FlowGraph graph, Term root) throws SemanticException {
      if (Report.should_report((String)"cfg", 2)) {
         this.dumpFlowGraph(graph, root);
      }

   }

   public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
      throw new InternalCompilerError("CopyPropagator.check should never be called.");
   }

   public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
      if (n instanceof Local) {
         FlowGraph g = this.currentFlowGraph();
         if (g == null) {
            return n;
         } else {
            Local l = (Local)n;
            Collection peers = g.peers(l);
            if (peers != null && !peers.isEmpty()) {
               List items = new ArrayList();
               Iterator it = peers.iterator();

               while(it.hasNext()) {
                  FlowGraph.Peer p = (FlowGraph.Peer)it.next();
                  if (p.inItem() != null) {
                     items.add(p.inItem());
                  }
               }

               CopyPropagator.DataFlowItem in = (CopyPropagator.DataFlowItem)this.confluence(items, l, g);
               if (in == null) {
                  return n;
               } else {
                  LocalInstance root = in.getRoot(l.localInstance());
                  if (root == null) {
                     return n;
                  } else {
                     return l.name(root.name()).localInstance(root);
                  }
               }
            } else {
               return n;
            }
         }
      } else if (n instanceof Unary) {
         return old;
      } else if (n instanceof Assign) {
         Assign oldAssign = (Assign)old;
         Assign newAssign = (Assign)n;
         return newAssign.left(oldAssign.left());
      } else {
         return n;
      }
   }

   protected static class DataFlowItem extends DataFlow.Item {
      private Map map;

      protected DataFlowItem() {
         this.map = new HashMap();
      }

      protected DataFlowItem(CopyPropagator.DataFlowItem dfi) {
         this.map = new HashMap(dfi.map.size());
         Iterator it = dfi.map.entrySet().iterator();

         while(it.hasNext()) {
            Entry e = (Entry)it.next();
            LocalInstance li = (LocalInstance)e.getKey();
            CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)e.getValue();
            if (ci.from != null) {
               this.add(ci.from.li, li);
            }
         }

      }

      protected void add(LocalInstance from, LocalInstance to) {
         boolean newTo = !this.map.containsKey(to);
         CopyPropagator.DataFlowItem.CopyInfo ciTo;
         if (newTo) {
            ciTo = new CopyPropagator.DataFlowItem.CopyInfo(to);
            this.map.put(to, ciTo);
         } else {
            ciTo = (CopyPropagator.DataFlowItem.CopyInfo)this.map.get(to);
         }

         CopyPropagator.DataFlowItem.CopyInfo ciFrom;
         if (this.map.containsKey(from)) {
            ciFrom = (CopyPropagator.DataFlowItem.CopyInfo)this.map.get(from);
         } else {
            ciFrom = new CopyPropagator.DataFlowItem.CopyInfo(from);
            this.map.put(from, ciFrom);
            ciFrom.root = ciFrom;
         }

         if (ciTo.from != null) {
            throw new InternalCompilerError("Error while copying dataflow item during copy propagation.");
         } else {
            ciFrom.to.add(ciTo);
            ciTo.from = ciFrom;
            if (newTo) {
               ciTo.root = ciFrom.root;
            } else {
               ciTo.setRoot(ciFrom.root);
            }

         }
      }

      protected void intersect(CopyPropagator.DataFlowItem dfi) {
         boolean modified = false;
         Iterator it = this.map.entrySet().iterator();

         while(true) {
            Entry e;
            while(it.hasNext()) {
               e = (Entry)it.next();
               LocalInstance li = (LocalInstance)e.getKey();
               CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)e.getValue();
               CopyPropagator.DataFlowItem.CopyInfo toCI;
               if (!dfi.map.containsKey(li)) {
                  modified = true;
                  it.remove();
                  if (ci.from != null) {
                     ci.from.to.remove(ci);
                  }

                  for(Iterator i = ci.to.iterator(); i.hasNext(); toCI.from = null) {
                     toCI = (CopyPropagator.DataFlowItem.CopyInfo)i.next();
                  }
               } else if (ci.from != null) {
                  CopyPropagator.DataFlowItem.CopyInfo otherCI = (CopyPropagator.DataFlowItem.CopyInfo)dfi.map.get(li);
                  toCI = (CopyPropagator.DataFlowItem.CopyInfo)dfi.map.get(ci.from.li);
                  if (toCI == null || otherCI.root != toCI.root) {
                     modified = true;
                     ci.from.to.remove(ci);
                     ci.from = null;
                  }
               }
            }

            if (!modified) {
               return;
            }

            it = this.map.entrySet().iterator();

            while(it.hasNext()) {
               e = (Entry)it.next();
               CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)e.getValue();
               if (ci.from == null) {
                  if (ci.to.isEmpty()) {
                     it.remove();
                  } else {
                     ci.setRoot(ci);
                  }
               }
            }

            return;
         }
      }

      public void kill(LocalInstance var) {
         if (this.map.containsKey(var)) {
            CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)this.map.get(var);
            this.map.remove(var);
            if (ci.from != null) {
               ci.from.to.remove(ci);
            }

            Iterator it = ci.to.iterator();

            while(it.hasNext()) {
               CopyPropagator.DataFlowItem.CopyInfo toCI = (CopyPropagator.DataFlowItem.CopyInfo)it.next();
               toCI.from = ci.from;
               if (ci.from == null) {
                  toCI.setRoot(toCI);
               } else {
                  ci.from.to.add(toCI);
               }
            }

         }
      }

      public LocalInstance getRoot(LocalInstance var) {
         return !this.map.containsKey(var) ? null : ((CopyPropagator.DataFlowItem.CopyInfo)this.map.get(var)).root.li;
      }

      private void die() {
         throw new InternalCompilerError("Copy propagation dataflow item consistency error.");
      }

      private void consistencyCheck() {
         Iterator it = this.map.entrySet().iterator();

         while(it.hasNext()) {
            Entry e = (Entry)it.next();
            LocalInstance li = (LocalInstance)e.getKey();
            CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)e.getValue();
            if (li != ci.li) {
               this.die();
            }

            if (!this.map.containsKey(ci.root.li)) {
               this.die();
            }

            if (this.map.get(ci.root.li) != ci.root) {
               this.die();
            }

            if (ci.from == null) {
               if (ci.root != ci) {
                  this.die();
               }
            } else {
               if (!this.map.containsKey(ci.from.li)) {
                  this.die();
               }

               if (this.map.get(ci.from.li) != ci.from) {
                  this.die();
               }

               if (ci.from.root != ci.root) {
                  this.die();
               }

               if (!ci.from.to.contains(ci)) {
                  this.die();
               }
            }

            Iterator i = ci.to.iterator();

            while(i.hasNext()) {
               CopyPropagator.DataFlowItem.CopyInfo toCI = (CopyPropagator.DataFlowItem.CopyInfo)i.next();
               if (!this.map.containsKey(toCI.li)) {
                  this.die();
               }

               if (this.map.get(toCI.li) != toCI) {
                  this.die();
               }

               if (toCI.root != ci.root) {
                  this.die();
               }

               if (toCI.from != ci) {
                  this.die();
               }
            }
         }

      }

      public int hashCode() {
         int result = 0;

         Entry e;
         for(Iterator it = this.map.entrySet().iterator(); it.hasNext(); result = 31 * result + e.getValue().hashCode()) {
            e = (Entry)it.next();
            result = 31 * result + e.getKey().hashCode();
         }

         return result;
      }

      public boolean equals(Object o) {
         if (!(o instanceof CopyPropagator.DataFlowItem)) {
            return false;
         } else {
            CopyPropagator.DataFlowItem dfi = (CopyPropagator.DataFlowItem)o;
            return this.map.equals(dfi.map);
         }
      }

      public String toString() {
         String result = "";
         boolean first = true;
         Iterator it = this.map.values().iterator();

         while(it.hasNext()) {
            CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)it.next();
            if (ci.from != null) {
               if (!first) {
                  result = result + ", ";
               }

               if (ci.root != ci.from) {
                  result = result + ci.root.li + " ->* ";
               }

               result = result + ci.from.li + " -> " + ci.li;
               first = false;
            }
         }

         return "[" + result + "]";
      }

      protected static class CopyInfo {
         final LocalInstance li;
         CopyPropagator.DataFlowItem.CopyInfo from;
         Set to;
         CopyPropagator.DataFlowItem.CopyInfo root;

         protected CopyInfo(LocalInstance li) {
            if (li == null) {
               throw new InternalCompilerError("Null local instance encountered during copy propagation.");
            } else {
               this.li = li;
               this.from = null;
               this.to = new HashSet();
               this.root = this;
            }
         }

         protected void setRoot(CopyPropagator.DataFlowItem.CopyInfo root) {
            List worklist = new ArrayList();
            worklist.add(this);

            while(worklist.size() > 0) {
               CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)worklist.remove(worklist.size() - 1);
               worklist.addAll(ci.to);
               ci.root = root;
            }

         }

         public boolean equals(Object o) {
            if (!(o instanceof CopyPropagator.DataFlowItem.CopyInfo)) {
               return false;
            } else {
               CopyPropagator.DataFlowItem.CopyInfo ci = (CopyPropagator.DataFlowItem.CopyInfo)o;
               boolean var10000;
               if (this.li == ci.li) {
                  label25: {
                     if (this.from == null) {
                        if (ci.from != null) {
                           break label25;
                        }
                     } else if (ci.from == null || this.from.li != ci.from.li) {
                        break label25;
                     }

                     if (this.root.li == ci.root.li) {
                        var10000 = true;
                        return var10000;
                     }
                  }
               }

               var10000 = false;
               return var10000;
            }
         }

         public int hashCode() {
            return this.li.hashCode() + 31 * (this.from == null ? 0 : this.from.li.hashCode() + 31 * this.root.li.hashCode());
         }
      }
   }
}
