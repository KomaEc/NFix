package org.testng.reporters;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class DotTestListener extends TestListenerAdapter {
   private int m_count = 0;

   public void onTestFailure(ITestResult tr) {
      this.log("F");
   }

   public void onTestSkipped(ITestResult tr) {
      this.log("S");
   }

   public void onTestSuccess(ITestResult tr) {
      this.log(".");
   }

   private void log(String string) {
      System.out.print(string);
      if (this.m_count++ % 40 == 0) {
         System.out.println("");
      }

   }
}
