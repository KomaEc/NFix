package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.HierarchicalClassId;

public class ClassHistory {
   private final HierarchicalClassId id;
   private final String coverageId;

   public ClassHistory(HierarchicalClassId id, String coverageId) {
      this.id = id;
      this.coverageId = coverageId;
   }

   public HierarchicalClassId getId() {
      return this.id;
   }

   public String getCoverageId() {
      return this.coverageId;
   }

   public ClassName getName() {
      return this.id.getName();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.coverageId == null ? 0 : this.coverageId.hashCode());
      result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
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
         ClassHistory other = (ClassHistory)obj;
         if (this.coverageId == null) {
            if (other.coverageId != null) {
               return false;
            }
         } else if (!this.coverageId.equals(other.coverageId)) {
            return false;
         }

         if (this.id == null) {
            if (other.id != null) {
               return false;
            }
         } else if (!this.id.equals(other.id)) {
            return false;
         }

         return true;
      }
   }
}
