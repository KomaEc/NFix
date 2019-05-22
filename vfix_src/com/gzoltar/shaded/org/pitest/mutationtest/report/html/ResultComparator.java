package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;

class ResultComparator implements Comparator<MutationResult>, Serializable {
   private static final long serialVersionUID = 1L;
   private static final EnumMap<DetectionStatus, Integer> RANK = new EnumMap(DetectionStatus.class);

   public int compare(MutationResult o1, MutationResult o2) {
      return this.getRanking(o1.getStatus()) - this.getRanking(o2.getStatus());
   }

   private int getRanking(DetectionStatus status) {
      return (Integer)RANK.get(status);
   }

   static {
      RANK.put(DetectionStatus.KILLED, 4);
      RANK.put(DetectionStatus.SURVIVED, 0);
      RANK.put(DetectionStatus.TIMED_OUT, 2);
      RANK.put(DetectionStatus.NON_VIABLE, 3);
      RANK.put(DetectionStatus.MEMORY_ERROR, 1);
      RANK.put(DetectionStatus.NOT_STARTED, 1);
      RANK.put(DetectionStatus.STARTED, 1);
      RANK.put(DetectionStatus.RUN_ERROR, 0);
      RANK.put(DetectionStatus.NO_COVERAGE, 0);
   }
}
