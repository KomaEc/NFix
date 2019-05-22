package com.gzoltar.shaded.org.pitest.classinfo;

import java.math.BigInteger;

public final class HierarchicalClassId {
   private final ClassIdentifier classId;
   private final String hierarchicalHash;

   public HierarchicalClassId(ClassIdentifier classId, String hierarchicalHash) {
      this.classId = classId;
      this.hierarchicalHash = hierarchicalHash;
   }

   public HierarchicalClassId(ClassIdentifier id, BigInteger deepHash) {
      this(id, deepHash.toString(16));
   }

   public HierarchicalClassId(long hash, ClassName name, String hierarchicalHash) {
      this(new ClassIdentifier(hash, name), hierarchicalHash);
   }

   public String getHierarchicalHash() {
      return this.hierarchicalHash;
   }

   public ClassName getName() {
      return this.classId.getName();
   }

   public ClassIdentifier getId() {
      return this.classId;
   }

   public String toString() {
      return "HierarchicalClassId [classId=" + this.classId + ", hierarchicalHash=" + this.hierarchicalHash + "]";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.classId == null ? 0 : this.classId.hashCode());
      result = 31 * result + (this.hierarchicalHash == null ? 0 : this.hierarchicalHash.hashCode());
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
         HierarchicalClassId other = (HierarchicalClassId)obj;
         if (this.classId == null) {
            if (other.classId != null) {
               return false;
            }
         } else if (!this.classId.equals(other.classId)) {
            return false;
         }

         if (this.hierarchicalHash == null) {
            if (other.hierarchicalHash != null) {
               return false;
            }
         } else if (!this.hierarchicalHash.equals(other.hierarchicalHash)) {
            return false;
         }

         return true;
      }
   }
}
