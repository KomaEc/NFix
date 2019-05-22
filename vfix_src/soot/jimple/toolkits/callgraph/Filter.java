package soot.jimple.toolkits.callgraph;

import java.util.Iterator;

public class Filter implements Iterator<Edge> {
   private Iterator<Edge> source;
   private EdgePredicate pred;
   private Edge next = null;

   public Filter(EdgePredicate pred) {
      this.pred = pred;
   }

   public Iterator<Edge> wrap(Iterator<Edge> source) {
      this.source = source;
      this.advance();
      return this;
   }

   private void advance() {
      while(true) {
         if (this.source.hasNext()) {
            this.next = (Edge)this.source.next();
            if (!this.pred.want(this.next)) {
               continue;
            }

            return;
         }

         this.next = null;
         return;
      }
   }

   public boolean hasNext() {
      return this.next != null;
   }

   public Edge next() {
      Edge ret = this.next;
      this.advance();
      return ret;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
