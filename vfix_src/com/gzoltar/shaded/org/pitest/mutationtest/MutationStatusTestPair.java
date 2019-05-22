package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.functional.Option;

public final class MutationStatusTestPair {
   private final int numberOfTestsRun;
   private final DetectionStatus status;
   private final Option<String> killingTest;

   public MutationStatusTestPair(int numberOfTestsRun, DetectionStatus status) {
      this(numberOfTestsRun, status, (String)null);
   }

   public MutationStatusTestPair(int numberOfTestsRun, DetectionStatus status, String killingTest) {
      this.status = status;
      this.killingTest = Option.some(killingTest);
      this.numberOfTestsRun = numberOfTestsRun;
   }

   public DetectionStatus getStatus() {
      return this.status;
   }

   public Option<String> getKillingTest() {
      return this.killingTest;
   }

   public int getNumberOfTestsRun() {
      return this.numberOfTestsRun;
   }

   public String toString() {
      return this.killingTest.hasNone() ? this.status.name() : this.status.name() + " by " + (String)this.killingTest.value();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.killingTest == null ? 0 : this.killingTest.hashCode());
      result = 31 * result + this.numberOfTestsRun;
      result = 31 * result + (this.status == null ? 0 : this.status.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         MutationStatusTestPair other = (MutationStatusTestPair)obj;
         if (this.killingTest == null) {
            if (other.killingTest != null) {
               return false;
            }
         } else if (!this.killingTest.equals(other.killingTest)) {
            return false;
         }

         if (this.numberOfTestsRun != other.numberOfTestsRun) {
            return false;
         } else {
            return this.status == other.status;
         }
      }
   }
}
