package fj.function;

public interface Try2<A, B, C, Z extends Exception> {
   C f(A var1, B var2) throws Z;
}
