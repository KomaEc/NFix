package com.gzoltar.shaded.org.pitest.classinfo;

public final class ClassIdentifier {
   private final long hash;
   private final ClassName name;

   public ClassIdentifier(long hash, ClassName name) {
      this.hash = hash;
      this.name = name;
   }

   public long getHash() {
      return this.hash;
   }

   public ClassName getName() {
      return this.name;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (int)(this.hash ^ this.hash >>> 32);
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
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
         ClassIdentifier other = (ClassIdentifier)obj;
         if (this.hash != other.hash) {
            return false;
         } else {
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            return true;
         }
      }
   }
}
