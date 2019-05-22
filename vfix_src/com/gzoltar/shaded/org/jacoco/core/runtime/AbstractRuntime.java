package com.gzoltar.shaded.org.jacoco.core.runtime;

import java.util.Random;

public abstract class AbstractRuntime implements IRuntime {
   protected RuntimeData data;
   private static final Random RANDOM = new Random();

   public void startup(RuntimeData data) throws Exception {
      this.data = data;
   }

   public static String createRandomId() {
      return Integer.toHexString(RANDOM.nextInt());
   }
}
