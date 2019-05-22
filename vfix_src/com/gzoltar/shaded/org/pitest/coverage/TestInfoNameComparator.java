package com.gzoltar.shaded.org.pitest.coverage;

import java.io.Serializable;
import java.util.Comparator;

class TestInfoNameComparator implements Comparator<TestInfo>, Serializable {
   private static final long serialVersionUID = 1L;

   public int compare(TestInfo lhs, TestInfo rhs) {
      return lhs.getName().compareTo(rhs.getName());
   }
}
