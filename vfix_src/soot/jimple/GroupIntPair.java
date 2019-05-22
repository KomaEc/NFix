package soot.jimple;

public class GroupIntPair {
   public Object group;
   public int x;

   public GroupIntPair(Object group, int x) {
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

   public String toString() {
      return this.group + ": " + this.x;
   }
}
