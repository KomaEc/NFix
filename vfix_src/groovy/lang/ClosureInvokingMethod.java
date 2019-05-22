package groovy.lang;

public interface ClosureInvokingMethod {
   Closure getClosure();

   boolean isStatic();

   String getName();
}
