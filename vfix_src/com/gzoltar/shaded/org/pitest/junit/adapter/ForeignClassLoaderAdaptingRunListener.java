package com.gzoltar.shaded.org.pitest.junit.adapter;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Fail;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Skipped;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Start;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Success;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

class ForeignClassLoaderAdaptingRunListener extends RunListener {
   private final List<String> events;
   private boolean finished = false;

   public ForeignClassLoaderAdaptingRunListener(List<String> queue) {
      this.events = queue;
   }

   public void testFailure(Failure failure) throws Exception {
      this.storeAsString(new Fail(failure.getException()));
      this.finished = true;
   }

   public void testAssumptionFailure(Failure failure) {
   }

   public void testIgnored(Description description) throws Exception {
      this.storeAsString(new Skipped());
      this.finished = true;
   }

   public void testStarted(Description description) throws Exception {
      this.storeAsString(new Start());
   }

   public void testFinished(Description description) throws Exception {
      if (!this.finished) {
         this.storeAsString(new Success());
      }

   }

   private void storeAsString(SideEffect2<ResultCollector, com.gzoltar.shaded.org.pitest.testapi.Description> result) {
      this.events.add(IsolationUtils.toXml(result));
   }
}
