package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;

public final class ClassLine {
   private final ClassName clazz;
   private final int lineNumber;

   public ClassLine(String clazz, int lineNumber) {
      this(ClassName.fromString(clazz), lineNumber);
   }

   public ClassLine(ClassName clazz, int lineNumber) {
      this.clazz = clazz;
      this.lineNumber = lineNumber;
   }

   public ClassName getClassName() {
      return this.clazz;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.clazz == null ? 0 : this.clazz.hashCode());
      result = 31 * result + this.lineNumber;
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
         ClassLine other = (ClassLine)obj;
         if (this.clazz == null) {
            if (other.clazz != null) {
               return false;
            }
         } else if (!this.clazz.equals(other.clazz)) {
            return false;
         }

         return this.lineNumber == other.lineNumber;
      }
   }

   public String toString() {
      return "ClassLine [" + this.clazz + ":" + this.lineNumber + "]";
   }
}
