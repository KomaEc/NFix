package com.gzoltar.shaded.org.pitest.testapi.execute;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;

public class ExitingResultCollector implements ResultCollector {
   private final ResultCollector child;
   private boolean hadFailure = false;

   public ExitingResultCollector(ResultCollector child) {
      this.child = child;
   }

   public void notifySkipped(Description description) {
      this.child.notifySkipped(description);
   }

   public void notifyStart(Description description) {
      this.child.notifyStart(description);
   }

   public boolean shouldExit() {
      return this.hadFailure;
   }

   public void notifyEnd(Description description, Throwable t) {
      this.child.notifyEnd(description, t);
      if (t != null) {
         this.hadFailure = true;
      }

   }

   public void notifyEnd(Description description) {
      this.child.notifyEnd(description);
   }
}
