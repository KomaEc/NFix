package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;

public final class MutationResult {
   private final MutationDetails details;
   private final MutationStatusTestPair status;

   public MutationResult(MutationDetails md, MutationStatusTestPair status) {
      this.details = md;
      this.status = status;
   }

   public MutationDetails getDetails() {
      return this.details;
   }

   public Option<String> getKillingTest() {
      return this.status.getKillingTest();
   }

   public DetectionStatus getStatus() {
      return this.status.getStatus();
   }

   public int getNumberOfTestsRun() {
      return this.status.getNumberOfTestsRun();
   }

   public MutationStatusTestPair getStatusTestPair() {
      return this.status;
   }

   public String getStatusDescription() {
      return this.getStatus().name();
   }

   public String getKillingTestDescription() {
      return (String)this.getKillingTest().getOrElse("none");
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.details == null ? 0 : this.details.hashCode());
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
         MutationResult other = (MutationResult)obj;
         if (this.details == null) {
            if (other.details != null) {
               return false;
            }
         } else if (!this.details.equals(other.details)) {
            return false;
         }

         if (this.status == null) {
            if (other.status != null) {
               return false;
            }
         } else if (!this.status.equals(other.status)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "MutationResult [details=" + this.details + ", status=" + this.status + "]";
   }
}
