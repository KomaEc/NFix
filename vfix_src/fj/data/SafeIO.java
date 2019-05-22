package fj.data;

public interface SafeIO<A> extends IO<A> {
   A run();
}
