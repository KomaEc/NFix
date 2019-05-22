package org.testng.util;

import java.util.concurrent.atomic.AtomicInteger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public abstract class RetryAnalyzerCount implements IRetryAnalyzer {
   AtomicInteger count = new AtomicInteger(1);

   protected void setCount(int count) {
      this.count.set(count);
   }

   protected int getCount() {
      return this.count.get();
   }

   public boolean retry(ITestResult result) {
      return this.count.getAndDecrement() > 0 ? this.retryMethod(result) : false;
   }

   public abstract boolean retryMethod(ITestResult var1);
}
