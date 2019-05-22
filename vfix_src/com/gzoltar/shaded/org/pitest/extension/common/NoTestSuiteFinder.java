package com.gzoltar.shaded.org.pitest.extension.common;

import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import java.util.Collections;
import java.util.List;

public class NoTestSuiteFinder implements TestSuiteFinder {
   public List<Class<?>> apply(Class<?> a) {
      return Collections.emptyList();
   }
}
