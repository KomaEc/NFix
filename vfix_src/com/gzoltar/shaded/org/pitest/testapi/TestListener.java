package com.gzoltar.shaded.org.pitest.testapi;

public interface TestListener {
   void onRunStart();

   void onTestStart(Description var1);

   void onTestFailure(TestResult var1);

   void onTestSkipped(TestResult var1);

   void onTestSuccess(TestResult var1);

   void onRunEnd();
}
