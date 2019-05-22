package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Fail;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Start;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Success;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import java.util.List;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class ForeignClassLoaderAdaptingListener implements ITestListener {
   private final List<String> events;
   private Throwable error;
   private boolean hasHadFailure = false;

   public ForeignClassLoaderAdaptingListener(List<String> events) {
      this.events = events;
   }

   public void onFinish(ITestContext arg0) {
      if (this.error != null) {
         this.storeAsString(new Fail(this.error));
      } else {
         this.storeAsString(new Success());
      }

   }

   public void onStart(ITestContext arg0) {
      this.storeAsString(new Start());
   }

   public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
      this.storeAsString(new TestSuccess(arg0.getMethod().getMethodName()));
   }

   public void onTestFailure(ITestResult arg0) {
      this.hasHadFailure = true;
      this.error = arg0.getThrowable();
      this.storeAsString(new TestFail(arg0.getMethod().getMethodName(), this.error));
   }

   public void onTestSkipped(ITestResult arg0) {
      this.storeAsString(new TestSkipped(arg0.getMethod().getMethodName()));
   }

   public void onTestStart(ITestResult result) {
      if (this.hasHadFailure) {
         throw new SkipException("skipping");
      } else {
         this.storeAsString(new TestStart(result.getMethod().getMethodName()));
      }
   }

   public void onTestSuccess(ITestResult arg0) {
      this.storeAsString(new TestSuccess(arg0.getMethod().getMethodName()));
   }

   private void storeAsString(SideEffect2<ResultCollector, Description> result) {
      this.events.add(IsolationUtils.toXml(result));
   }
}
