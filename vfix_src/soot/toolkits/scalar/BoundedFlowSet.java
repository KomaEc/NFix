package soot.toolkits.scalar;

public interface BoundedFlowSet<T> extends FlowSet<T> {
   void complement();

   void complement(FlowSet<T> var1);

   FlowSet<T> topSet();
}
