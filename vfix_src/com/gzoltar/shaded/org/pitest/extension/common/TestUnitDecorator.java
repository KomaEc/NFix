package com.gzoltar.shaded.org.pitest.extension.common;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;

public abstract class TestUnitDecorator implements TestUnit {
   private final TestUnit child;

   protected TestUnitDecorator(TestUnit child) {
      this.child = child;
   }

   public Description getDescription() {
      return this.child.getDescription();
   }

   public TestUnit child() {
      return this.child;
   }

   public abstract void execute(ClassLoader var1, ResultCollector var2);
}
