package org.testng.internal.thread;

import java.io.Serializable;

public interface IAtomicInteger extends Serializable {
   int get();

   int incrementAndGet();
}
