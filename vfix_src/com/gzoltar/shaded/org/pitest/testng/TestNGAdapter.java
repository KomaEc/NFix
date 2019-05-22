package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class TestNGAdapter implements ITestListener {
   private final ResultCollector rc;
   private final Description description;
   private final Class<?> clazz;
   private boolean hasHadFailure = false;
   private Throwable error;

   public TestNGAdapter(Class<?> clazz, Description d, ResultCollector rc) {
      this.rc = rc;
      this.description = d;
      this.clazz = clazz;
   }

   public void onFinish(ITestContext arg0) {
      if (this.error != null) {
         this.rc.notifyEnd(this.description, this.error);
      } else {
         this.rc.notifyEnd(this.description);
      }

   }

   public void onStart(ITestContext arg0) {
      this.rc.notifyStart(this.description);
   }

   public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
      this.rc.notifyEnd(this.makeDescription(arg0));
   }

   public void onTestFailure(ITestResult arg0) {
      this.hasHadFailure = true;
      this.error = arg0.getThrowable();
      this.rc.notifyEnd(this.makeDescription(arg0), this.error);
   }

   public void onTestSkipped(ITestResult arg0) {
      this.rc.notifySkipped(this.makeDescription(arg0));
   }

   public void onTestStart(ITestResult arg0) {
      if (this.hasHadFailure) {
         throw new SkipException("skipping");
      } else {
         this.rc.notifyStart(this.makeDescription(arg0));
      }
   }

   public void onTestSuccess(ITestResult arg0) {
      this.rc.notifyEnd(this.makeDescription(arg0));
   }

   private Description makeDescription(ITestResult result) {
      return new Description(result.getMethod().getMethodName(), this.clazz);
   }
}
