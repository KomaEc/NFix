package fj.function;

public interface TryEffect2<A, B, Z extends Exception> {
   void f(A var1, B var2) throws Z;
}
