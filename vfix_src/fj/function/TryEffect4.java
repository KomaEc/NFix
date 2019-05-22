package fj.function;

public interface TryEffect4<A, B, C, D, Z extends Exception> {
   void f(A var1, B var2, C var3, D var4) throws Z;
}
