package com.gzoltar.shaded.org.pitest.testapi;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;

public abstract class BaseTestClassIdentifier implements TestClassIdentifier {
   public boolean isIncluded(ClassInfo a) {
      return true;
   }
}
