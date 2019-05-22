package soot.jimple.toolkits.infoflow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;

public class CallChain {
   Edge edge;
   CallChain next;

   public CallChain(Edge edge, CallChain next) {
      this.edge = edge;
      if (next != null && next.edge == null && next.next == null) {
         this.next = null;
      } else {
         this.next = next;
      }

   }

   public List<Edge> getEdges() {
      List<Edge> ret = new LinkedList();
      if (this.edge != null) {
         ret.add(this.edge);
      }

      for(CallChain current = this.next; current != null; current = current.next) {
         ret.add(current.edge);
      }

      return ret;
   }

   public int size() {
      return 1 + (this.next == null ? 0 : this.next.size());
   }

   public Iterator<Edge> iterator() {
      return this.getEdges().iterator();
   }

   public boolean contains(Edge e) {
      return this.edge == e || this.next != null && this.next.contains(e);
   }

   public boolean containsMethod(SootMethod sm) {
      return this.edge != null && this.edge.tgt() == sm || this.next != null && this.next.containsMethod(sm);
   }

   public CallChain cloneAndExtend(CallChain extension) {
      return this.next == null ? new CallChain(this.edge, extension) : new CallChain(this.edge, this.next.cloneAndExtend(extension));
   }

   public Object clone() {
      return this.next == null ? new CallChain(this.edge, (CallChain)null) : new CallChain(this.edge, (CallChain)this.next.clone());
   }

   public boolean equals(Object o) {
      if (o instanceof CallChain) {
         CallChain other = (CallChain)o;
         if (this.edge == other.edge && (this.next == null && other.next == null || this.next != null && other.next != null && this.next.equals(other.next))) {
            return true;
         }
      }

      return false;
   }
}
