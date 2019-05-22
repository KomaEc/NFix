package com.gzoltar.shaded.org.pitest.testapi.foreignclassloader;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;

public class Fail implements SideEffect2<ResultCollector, Description> {
   private final Throwable throwable;

   public Fail(Throwable throwable) {
      this.throwable = throwable;
   }

   public void apply(ResultCollector rc, Description description) {
      rc.notifyEnd(description, this.throwable);
   }
}
