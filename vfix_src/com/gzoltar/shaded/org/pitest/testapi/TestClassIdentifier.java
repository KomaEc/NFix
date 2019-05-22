package com.gzoltar.shaded.org.pitest.testapi;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;

public interface TestClassIdentifier {
   boolean isATestClass(ClassInfo var1);

   boolean isIncluded(ClassInfo var1);
}
