package polyglot.visit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Block;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;

public class ReachChecker extends DataFlow {
   public ReachChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf, true, true);
   }

   public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
      return node == graph.entryNode() ? ReachChecker.DataFlowItem.REACHABLE : ReachChecker.DataFlowItem.NOT_REACHABLE;
   }

   public Map flow(DataFlow.Item in, FlowGraph graph, Term n, Set succEdgeKeys) {
      if (in == ReachChecker.DataFlowItem.NOT_REACHABLE) {
         return itemToMap(in, succEdgeKeys);
      } else {
         Map m = itemToMap(ReachChecker.DataFlowItem.REACHABLE_EX_ONLY, succEdgeKeys);
         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_OTHER)) {
            m.put(FlowGraph.EDGE_KEY_OTHER, ReachChecker.DataFlowItem.REACHABLE);
         }

         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_TRUE)) {
            m.put(FlowGraph.EDGE_KEY_TRUE, ReachChecker.DataFlowItem.REACHABLE);
         }

         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_FALSE)) {
            m.put(FlowGraph.EDGE_KEY_FALSE, ReachChecker.DataFlowItem.REACHABLE);
         }

         return m;
      }
   }

   public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
      throw new InternalCompilerError("Should never be called.");
   }

   public DataFlow.Item confluence(List inItems, List itemKeys, Term node, FlowGraph graph) {
      List l = this.filterItemsNonException(inItems, itemKeys);
      Iterator i = l.iterator();

      do {
         if (!i.hasNext()) {
            i = inItems.iterator();

            do {
               if (!i.hasNext()) {
                  return ReachChecker.DataFlowItem.NOT_REACHABLE;
               }
            } while(!((ReachChecker.DataFlowItem)i.next()).reachable);

            return ReachChecker.DataFlowItem.REACHABLE_EX_ONLY;
         }
      } while(i.next() != ReachChecker.DataFlowItem.REACHABLE);

      return ReachChecker.DataFlowItem.REACHABLE;
   }

   public Node leaveCall(Node n) throws SemanticException {
      if (n instanceof Term) {
         n = this.checkReachability((Term)n);
      }

      return super.leaveCall(n);
   }

   protected Node checkReachability(Term n) throws SemanticException {
      FlowGraph g = this.currentFlowGraph();
      if (g != null) {
         Collection peers = g.peers(n);
         if (peers != null && !peers.isEmpty()) {
            boolean isInitializer = n instanceof Initializer;
            Iterator iter = peers.iterator();

            label59:
            while(true) {
               if (iter.hasNext()) {
                  FlowGraph.Peer p = (FlowGraph.Peer)iter.next();
                  if (p.inItem() != null) {
                     ReachChecker.DataFlowItem dfi = (ReachChecker.DataFlowItem)p.inItem();
                     if (isInitializer && !dfi.normalReachable) {
                        throw new SemanticException("Initializers must be able to complete normally.", n.position());
                     }

                     if (dfi.reachable) {
                        return n.reachable(true);
                     }
                  }

                  if (p.outItems == null) {
                     continue;
                  }

                  Iterator k = p.outItems.values().iterator();

                  ReachChecker.DataFlowItem item;
                  do {
                     if (!k.hasNext()) {
                        continue label59;
                     }

                     item = (ReachChecker.DataFlowItem)k.next();
                  } while(item == null || !item.reachable);

                  return n.reachable(true);
               }

               n = n.reachable(false);
               if (n instanceof Block && ((Block)n).statements().isEmpty() || n instanceof Stmt && !(n instanceof CompoundStmt)) {
                  throw new SemanticException("Unreachable statement.", n.position());
               }
               break;
            }
         }
      }

      return n;
   }

   public void post(FlowGraph graph, Term root) throws SemanticException {
      if (Report.should_report((String)"cfg", 2)) {
         this.dumpFlowGraph(graph, root);
      }

   }

   public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
      throw new InternalCompilerError("ReachChecker.check should never be called.");
   }

   protected static class DataFlowItem extends DataFlow.Item {
      final boolean reachable;
      final boolean normalReachable;
      public static final ReachChecker.DataFlowItem REACHABLE = new ReachChecker.DataFlowItem(true, true);
      public static final ReachChecker.DataFlowItem REACHABLE_EX_ONLY = new ReachChecker.DataFlowItem(true, false);
      public static final ReachChecker.DataFlowItem NOT_REACHABLE = new ReachChecker.DataFlowItem(false, false);

      protected DataFlowItem(boolean reachable, boolean normalReachable) {
         this.reachable = reachable;
         this.normalReachable = normalReachable;
      }

      public String toString() {
         return (this.reachable ? "" : "not ") + "reachable" + (this.normalReachable ? "" : " by exceptions only");
      }

      public boolean equals(Object o) {
         if (!(o instanceof ReachChecker.DataFlowItem)) {
            return false;
         } else {
            return this.reachable == ((ReachChecker.DataFlowItem)o).reachable && this.normalReachable == ((ReachChecker.DataFlowItem)o).normalReachable;
         }
      }

      public int hashCode() {
         return (this.reachable ? 5423 : 5753) + (this.normalReachable ? 31 : -2);
      }
   }
}
