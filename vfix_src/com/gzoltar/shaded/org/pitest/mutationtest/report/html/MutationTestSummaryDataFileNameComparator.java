package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import java.io.Serializable;
import java.util.Comparator;

public class MutationTestSummaryDataFileNameComparator implements Comparator<MutationTestSummaryData>, Serializable {
   private static final long serialVersionUID = 1L;

   public int compare(MutationTestSummaryData arg0, MutationTestSummaryData arg1) {
      return arg0.getFileName().compareTo(arg1.getFileName());
   }
}
