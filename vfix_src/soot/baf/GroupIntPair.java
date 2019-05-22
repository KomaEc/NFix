package soot.baf;

class GroupIntPair {
   Object group;
   int x;

   GroupIntPair(Object group, int x) {
      this.group = group;
      this.x = x;
   }

   public boolean equals(Object other) {
      if (!(other instanceof GroupIntPair)) {
         return false;
      } else {
         return ((GroupIntPair)other).group.equals(this.group) && ((GroupIntPair)other).x == this.x;
      }
   }

   public int hashCode() {
      return this.group.hashCode() + 1013 * this.x;
   }
}
