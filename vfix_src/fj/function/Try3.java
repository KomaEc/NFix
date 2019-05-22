package fj.function;

public interface Try3<A, B, C, D, Z extends Exception> {
   D f(A var1, B var2, C var3) throws Z;
}
