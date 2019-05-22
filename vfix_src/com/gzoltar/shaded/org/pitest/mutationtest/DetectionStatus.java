package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.util.ExitCode;

public enum DetectionStatus {
   KILLED(true),
   SURVIVED(false),
   TIMED_OUT(true),
   NON_VIABLE(true),
   MEMORY_ERROR(true),
   NOT_STARTED(false),
   STARTED(false),
   RUN_ERROR(true),
   NO_COVERAGE(false);

   private final boolean detected;

   private DetectionStatus(boolean detected) {
      this.detected = detected;
   }

   public static DetectionStatus getForErrorExitCode(ExitCode exitCode) {
      if (exitCode.equals(ExitCode.OUT_OF_MEMORY)) {
         return MEMORY_ERROR;
      } else {
         return exitCode.equals(ExitCode.TIMEOUT) ? TIMED_OUT : RUN_ERROR;
      }
   }

   public boolean isDetected() {
      return this.detected;
   }
}
