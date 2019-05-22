package soot.toolkits.scalar;

class StringGroupPair {
   String string;
   Object group;

   public StringGroupPair(String s, Object g) {
      this.string = s;
      this.group = g;
   }

   public boolean equals(Object p) {
      if (!(p instanceof StringGroupPair)) {
         return false;
      } else {
         return ((StringGroupPair)p).string.equals(this.string) && ((StringGroupPair)p).group.equals(this.group);
      }
   }

   public int hashCode() {
      return this.string.hashCode() * 101 + this.group.hashCode() + 17;
   }
}
