package com.gzoltar.shaded.org.pitest.testapi;

public abstract class AbstractTestUnit implements TestUnit {
   private final Description description;

   public AbstractTestUnit(Description description) {
      this.description = description;
   }

   public abstract void execute(ClassLoader var1, ResultCollector var2);

   public final Description getDescription() {
      return this.description;
   }
}
