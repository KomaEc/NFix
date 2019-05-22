package polyglot.visit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.CodeDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.NodeFactory;
import polyglot.ast.Return;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class ExitChecker extends DataFlow {
   protected CodeDecl code;

   public ExitChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf, false);
   }

   protected FlowGraph initGraph(CodeDecl code, Term root) {
      this.code = code;
      if (code instanceof MethodDecl) {
         MethodDecl d = (MethodDecl)code;
         if (!d.methodInstance().returnType().isVoid()) {
            return super.initGraph(code, root);
         }
      }

      return null;
   }

   public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
      return ExitChecker.DataFlowItem.EXITS;
   }

   public Map flow(DataFlow.Item in, FlowGraph graph, Term n, Set succEdgeKeys) {
      if (n instanceof Return) {
         return itemToMap(ExitChecker.DataFlowItem.EXITS, succEdgeKeys);
      } else if (n == graph.exitNode()) {
         Map m = itemToMap(ExitChecker.DataFlowItem.EXITS, succEdgeKeys);
         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_OTHER)) {
            m.put(FlowGraph.EDGE_KEY_OTHER, ExitChecker.DataFlowItem.DOES_NOT_EXIT);
         }

         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_TRUE)) {
            m.put(FlowGraph.EDGE_KEY_TRUE, ExitChecker.DataFlowItem.DOES_NOT_EXIT);
         }

         if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_FALSE)) {
            m.put(FlowGraph.EDGE_KEY_FALSE, ExitChecker.DataFlowItem.DOES_NOT_EXIT);
         }

         return m;
      } else {
         return itemToMap(in, succEdgeKeys);
      }
   }

   public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
      Iterator i = inItems.iterator();

      do {
         if (!i.hasNext()) {
            return ExitChecker.DataFlowItem.EXITS;
         }
      } while(((ExitChecker.DataFlowItem)i.next()).exits);

      return ExitChecker.DataFlowItem.DOES_NOT_EXIT;
   }

   public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
      if (n == graph.entryNode() && outItems != null && !outItems.isEmpty()) {
         ExitChecker.DataFlowItem outItem = (ExitChecker.DataFlowItem)outItems.values().iterator().next();
         if (outItem != null && !outItem.exits) {
            throw new SemanticException("Missing return statement.", this.code.position());
         }
      }

   }

   protected static class DataFlowItem extends DataFlow.Item {
      final boolean exits;
      public static final ExitChecker.DataFlowItem EXITS = new ExitChecker.DataFlowItem(true);
      public static final ExitChecker.DataFlowItem DOES_NOT_EXIT = new ExitChecker.DataFlowItem(false);

      protected DataFlowItem(boolean exits) {
         this.exits = exits;
      }

      public String toString() {
         return "exits=" + this.exits;
      }

      public boolean equals(Object o) {
         if (o instanceof ExitChecker.DataFlowItem) {
            return this.exits == ((ExitChecker.DataFlowItem)o).exits;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.exits ? 5235 : 8673;
      }
   }
}
