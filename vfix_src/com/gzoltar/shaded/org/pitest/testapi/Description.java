package com.gzoltar.shaded.org.pitest.testapi;

public final class Description {
   private final String testClass;
   private final String name;

   public Description(String name) {
      this(name, (String)null);
   }

   public Description(String name, Class<?> testClass) {
      this(name, testClass.getName());
   }

   public Description(String name, String testClass) {
      this.testClass = this.internIfNotNull(testClass);
      this.name = name;
   }

   private String internIfNotNull(String string) {
      return string == null ? null : string.intern();
   }

   public String getFirstTestClass() {
      return this.testClass;
   }

   public String getQualifiedName() {
      return this.testClass != null ? this.getFirstTestClass() + "." + this.getName() : this.getName();
   }

   public String getName() {
      return this.name;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      result = 31 * result + (this.testClass == null ? 0 : this.testClass.hashCode());
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
         Description other = (Description)obj;
         if (this.name == null) {
            if (other.name != null) {
               return false;
            }
         } else if (!this.name.equals(other.name)) {
            return false;
         }

         if (this.testClass == null) {
            if (other.testClass != null) {
               return false;
            }
         } else if (!this.testClass.equals(other.testClass)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "Description [testClass=" + this.testClass + ", name=" + this.name + "]";
   }
}
