package com.gzoltar.shaded.org.pitest.dependency;

final class DependencyAccess {
   private final DependencyAccess.Member source;
   private final DependencyAccess.Member dest;

   protected DependencyAccess(DependencyAccess.Member source, DependencyAccess.Member dest) {
      this.source = source;
      this.dest = dest;
   }

   public DependencyAccess.Member getSource() {
      return this.source;
   }

   public DependencyAccess.Member getDest() {
      return this.dest;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.dest == null ? 0 : this.dest.hashCode());
      result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
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
         DependencyAccess other = (DependencyAccess)obj;
         if (this.dest == null) {
            if (other.dest != null) {
               return false;
            }
         } else if (!this.dest.equals(other.dest)) {
            return false;
         }

         if (this.source == null) {
            if (other.source != null) {
               return false;
            }
         } else if (!this.source.equals(other.source)) {
            return false;
         }

         return true;
      }
   }

   static class Member implements Comparable<DependencyAccess.Member> {
      private final String owner;
      private final String name;

      protected Member(String owner, String name) {
         this.owner = owner;
         this.name = name;
      }

      public String getOwner() {
         return this.owner;
      }

      public String getName() {
         return this.name;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
         result = 31 * result + (this.owner == null ? 0 : this.owner.hashCode());
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
            DependencyAccess.Member other = (DependencyAccess.Member)obj;
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.owner == null) {
               if (other.owner != null) {
                  return false;
               }
            } else if (!this.owner.equals(other.owner)) {
               return false;
            }

            return true;
         }
      }

      public int compareTo(DependencyAccess.Member other) {
         return other.name.compareTo(this.name) * 100 + other.owner.compareTo(this.owner) * 1000;
      }
   }
}
