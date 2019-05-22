package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.PointsToSet;

public class MemoryEfficientRasUnion extends Union {
   HashSet<PointsToSet> subsets;

   public boolean isEmpty() {
      if (this.subsets == null) {
         return true;
      } else {
         Iterator var1 = this.subsets.iterator();

         PointsToSet subset;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            subset = (PointsToSet)var1.next();
         } while(subset.isEmpty());

         return false;
      }
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      if (this.subsets == null) {
         return true;
      } else {
         Iterator var2 = this.subsets.iterator();

         while(var2.hasNext()) {
            PointsToSet subset = (PointsToSet)var2.next();
            if (other instanceof Union) {
               if (other.hasNonEmptyIntersection(subset)) {
                  return true;
               }
            } else if (subset.hasNonEmptyIntersection(other)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean addAll(PointsToSet s) {
      if (this.subsets == null) {
         this.subsets = new HashSet();
      }

      boolean result;
      if (s instanceof MemoryEfficientRasUnion) {
         MemoryEfficientRasUnion meru = (MemoryEfficientRasUnion)s;
         if (meru.subsets == null || this.subsets.containsAll(meru.subsets)) {
            return false;
         }

         result = this.subsets.addAll(meru.subsets);
      } else {
         result = this.subsets.add(s);
      }

      return result;
   }

   public Object clone() {
      MemoryEfficientRasUnion ret = new MemoryEfficientRasUnion();
      ret.addAll(this);
      return ret;
   }

   public Set possibleTypes() {
      if (this.subsets == null) {
         return Collections.EMPTY_SET;
      } else {
         HashSet ret = new HashSet();
         Iterator var2 = this.subsets.iterator();

         while(var2.hasNext()) {
            PointsToSet subset = (PointsToSet)var2.next();
            ret.addAll(subset.possibleTypes());
         }

         return ret;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = 31 * result + (this.subsets == null ? 0 : this.subsets.hashCode());
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
         MemoryEfficientRasUnion other = (MemoryEfficientRasUnion)obj;
         if (this.subsets == null) {
            if (other.subsets != null) {
               return false;
            }
         } else if (!this.subsets.equals(other.subsets)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.subsets == null ? "[]" : this.subsets.toString();
   }
}
