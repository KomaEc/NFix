package fj.data;

import java.io.IOException;

public interface IO<A> {
   A run() throws IOException;
}
