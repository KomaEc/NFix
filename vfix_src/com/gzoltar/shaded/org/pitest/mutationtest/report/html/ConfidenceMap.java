package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import java.util.EnumSet;

class ConfidenceMap {
   private static final EnumSet<DetectionStatus> HIGH;

   public static boolean hasHighConfidence(DetectionStatus status) {
      return HIGH.contains(status);
   }

   static {
      HIGH = EnumSet.of(DetectionStatus.KILLED, DetectionStatus.SURVIVED, DetectionStatus.NO_COVERAGE, DetectionStatus.NON_VIABLE);
   }
}
