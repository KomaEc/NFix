package heros;

public interface EdgeFunction<V> {
   V computeTarget(V var1);

   EdgeFunction<V> composeWith(EdgeFunction<V> var1);

   EdgeFunction<V> joinWith(EdgeFunction<V> var1);

   boolean equalTo(EdgeFunction<V> var1);
}
