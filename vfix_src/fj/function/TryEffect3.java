package fj.function;

public interface TryEffect3<A, B, C, Z extends Exception> {
   void f(A var1, B var2, C var3) throws Z;
}
