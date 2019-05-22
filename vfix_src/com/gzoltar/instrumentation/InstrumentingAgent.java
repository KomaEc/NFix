package com.gzoltar.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class InstrumentingAgent {
   public static Instrumentation instrumentationInstance;

   public static void premain(String var0, Instrumentation var1) {
      instrumentationInstance = var1;
   }

   public static void addTransformer(ClassFileTransformer var0) {
      instrumentationInstance.addTransformer(var0);
   }
}
