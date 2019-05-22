package com.gzoltar.shaded.org.pitest.testapi.execute.containers;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitState;
import java.util.Collection;

public final class ConcreteResultCollector implements ResultCollector {
   private final Collection<TestResult> feedback;

   public ConcreteResultCollector(Collection<TestResult> feedback) {
      this.feedback = feedback;
   }

   public void notifyStart(Description tu) {
      this.put(new TestResult(tu, (Throwable)null, TestUnitState.STARTED));
   }

   public void notifySkipped(Description tu) {
      this.put(new TestResult(tu, (Throwable)null, TestUnitState.NOT_RUN));
   }

   public void notifyEnd(Description description, Throwable t) {
      this.put(new TestResult(description, t));
   }

   public void notifyEnd(Description description) {
      this.put(new TestResult(description, (Throwable)null));
   }

   private void put(TestResult tr) {
      this.feedback.add(tr);
   }

   public boolean shouldExit() {
      return false;
   }
}
