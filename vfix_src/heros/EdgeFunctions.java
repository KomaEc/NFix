package heros;

public interface EdgeFunctions<N, D, M, V> {
   EdgeFunction<V> getNormalEdgeFunction(N var1, D var2, N var3, D var4);

   EdgeFunction<V> getCallEdgeFunction(N var1, D var2, M var3, D var4);

   EdgeFunction<V> getReturnEdgeFunction(N var1, M var2, N var3, D var4, N var5, D var6);

   EdgeFunction<V> getCallToReturnEdgeFunction(N var1, D var2, N var3, D var4);
}
