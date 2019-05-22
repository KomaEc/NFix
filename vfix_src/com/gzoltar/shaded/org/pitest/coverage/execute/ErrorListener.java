package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.TestListener;
import com.gzoltar.shaded.org.pitest.testapi.TestResult;

public class ErrorListener implements TestListener {
   public void onRunStart() {
   }

   public void onTestStart(Description d) {
   }

   public void onTestFailure(TestResult tr) {
      System.out.println("FAIL " + tr.getDescription() + " -> " + tr.getThrowable());
   }

   public void onTestSkipped(TestResult tr) {
   }

   public void onTestSuccess(TestResult tr) {
   }

   public void onRunEnd() {
   }
}
