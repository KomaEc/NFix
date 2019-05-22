package fj.function;

public interface Try1<A, B, Z extends Exception> {
   B f(A var1) throws Z;
}
