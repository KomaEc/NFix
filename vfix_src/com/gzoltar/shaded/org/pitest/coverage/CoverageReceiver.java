package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.testapi.Description;
import sun.pitest.InvokeReceiver;

public interface CoverageReceiver extends InvokeReceiver {
   void newTest();

   void recordTestOutcome(Description var1, boolean var2, int var3);
}
