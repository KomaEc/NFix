package heros;

public interface FlowFunctions<N, D, M> {
   FlowFunction<D> getNormalFlowFunction(N var1, N var2);

   FlowFunction<D> getCallFlowFunction(N var1, M var2);

   FlowFunction<D> getReturnFlowFunction(N var1, M var2, N var3, N var4);

   FlowFunction<D> getCallToReturnFlowFunction(N var1, N var2);
}
