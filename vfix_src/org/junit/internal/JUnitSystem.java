package org.junit.internal;

import java.io.PrintStream;

public interface JUnitSystem {
   /** @deprecated */
   @Deprecated
   void exit(int var1);

   PrintStream out();
}
