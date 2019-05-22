package polyglot.util;

class MaxLevels {
   int maxLevel;
   int maxLevelInner;

   MaxLevels(int ml, int mli) {
      this.maxLevel = ml;
      this.maxLevelInner = mli;
   }

   public int hashCode() {
      return this.maxLevel * 17 + this.maxLevelInner;
   }

   public boolean equals(Object o) {
      if (!(o instanceof MaxLevels)) {
         return false;
      } else {
         MaxLevels m2 = (MaxLevels)o;
         return this.maxLevel == m2.maxLevel && this.maxLevelInner == m2.maxLevelInner;
      }
   }

   public String toString() {
      return "[" + this.maxLevel + "/" + this.maxLevelInner + "]";
   }
}
