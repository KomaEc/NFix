package soot.toolkits.scalar;

public abstract class AbstractBoundedFlowSet<T> extends AbstractFlowSet<T> implements BoundedFlowSet<T> {
   public void complement() {
      this.complement(this);
   }

   public void complement(FlowSet<T> dest) {
      if (this == dest) {
         this.complement();
      } else {
         BoundedFlowSet<T> tmp = (BoundedFlowSet)this.topSet();
         tmp.difference(this, dest);
      }

   }

   public FlowSet<T> topSet() {
      BoundedFlowSet<T> tmp = (BoundedFlowSet)this.emptySet();
      tmp.complement();
      return tmp;
   }
}
