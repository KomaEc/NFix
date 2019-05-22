package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.process.LaunchOptions;

public final class MutationConfig {
   private final LaunchOptions launchOptions;
   private final MutationEngine engine;

   public MutationConfig(MutationEngine engine, LaunchOptions launchOptions) {
      this.launchOptions = launchOptions;
      this.engine = engine;
   }

   public Mutater createMutator(ClassByteArraySource source) {
      return this.engine.createMutator(source);
   }

   public MutationEngine getEngine() {
      return this.engine;
   }

   public LaunchOptions getLaunchOptions() {
      return this.launchOptions;
   }

   public boolean equals(Object rhs) {
      throw new UnsupportedOperationException();
   }

   public int hashCode() {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      return "MutationConfig [launchOptions=" + this.launchOptions + ", engine=" + this.engine + "]";
   }
}
