package com.gzoltar.shaded.org.pitest.mutationtest.build;

import java.io.Serializable;
import java.util.Comparator;

class AnalysisPriorityComparator implements Comparator<MutationAnalysisUnit>, Serializable {
   private static final long serialVersionUID = 1L;

   public int compare(MutationAnalysisUnit a, MutationAnalysisUnit b) {
      return b.priority() - a.priority();
   }
}
