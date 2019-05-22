package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;

class TestSuccess implements SideEffect2<ResultCollector, Description> {
   private final String methodName;

   TestSuccess(String methodName) {
      this.methodName = methodName;
   }

   public void apply(ResultCollector rc, Description d) {
      rc.notifyEnd(new Description(this.methodName, d.getFirstTestClass()));
   }
}
