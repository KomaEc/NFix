package com.gzoltar.shaded.org.pitest.testapi;

public interface ResultCollector {
   void notifyEnd(Description var1, Throwable var2);

   void notifyEnd(Description var1);

   void notifyStart(Description var1);

   void notifySkipped(Description var1);

   boolean shouldExit();
}
