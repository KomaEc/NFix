package org.codehaus.classworlds;

class Entry implements Comparable {
   private final ClassRealm realm;
   private final String pkgName;

   Entry(ClassRealm realm, String pkgName) {
      this.realm = realm;
      this.pkgName = pkgName;
   }

   ClassRealm getRealm() {
      return this.realm;
   }

   String getPackageName() {
      return this.pkgName;
   }

   boolean matches(String classname) {
      return classname.startsWith(this.getPackageName());
   }

   public int compareTo(Object thatObj) {
      Entry that = (Entry)thatObj;
      return this.getPackageName().compareTo(that.getPackageName()) * -1;
   }

   public boolean equals(Object thatObj) {
      Entry that = (Entry)thatObj;
      return this.getPackageName().equals(that.getPackageName());
   }

   public int hashCode() {
      return this.getPackageName().hashCode();
   }
}
