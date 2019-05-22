package com.gzoltar.shaded.org.pitest.boot;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class HotSwapAgent {
   private static Instrumentation instrumentation;

   public static void premain(String agentArguments, Instrumentation inst) {
      System.out.println("Installing PIT agent");
      instrumentation = inst;
   }

   public static void addTransformer(ClassFileTransformer transformer) {
      instrumentation.addTransformer(transformer);
   }

   public static void agentmain(String agentArguments, Instrumentation inst) throws Exception {
      instrumentation = inst;
   }

   public static boolean hotSwap(Class<?> mutateMe, byte[] bytes) {
      ClassDefinition[] definitions = new ClassDefinition[]{new ClassDefinition(mutateMe, bytes)};

      try {
         instrumentation.redefineClasses(definitions);
         return true;
      } catch (ClassNotFoundException var4) {
      } catch (UnmodifiableClassException var5) {
      } catch (VerifyError var6) {
      } catch (InternalError var7) {
      }

      return false;
   }
}
