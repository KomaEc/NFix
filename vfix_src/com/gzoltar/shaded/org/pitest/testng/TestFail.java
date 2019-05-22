package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;

class TestFail implements SideEffect2<ResultCollector, Description> {
   private final String methodName;
   private final Throwable throwable;

   TestFail(String methodName, Throwable throwable) {
      this.methodName = methodName;
      this.throwable = throwable;
   }

   public void apply(ResultCollector rc, Description d) {
      rc.notifyEnd(new Description(this.methodName, d.getFirstTestClass()), this.throwable);
   }
}
