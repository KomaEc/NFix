package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;

class TestSkipped implements SideEffect2<ResultCollector, Description> {
   private final String methodName;

   TestSkipped(String methodName) {
      this.methodName = methodName;
   }

   public void apply(ResultCollector rc, Description d) {
      rc.notifySkipped(new Description(this.methodName, d.getFirstTestClass()));
   }
}
