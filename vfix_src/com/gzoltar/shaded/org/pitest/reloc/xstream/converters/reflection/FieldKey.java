package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

public class FieldKey {
   private final String fieldName;
   private final Class declaringClass;
   private final int depth;
   private final int order;

   public FieldKey(String fieldName, Class declaringClass, int order) {
      if (fieldName != null && declaringClass != null) {
         this.fieldName = fieldName;
         this.declaringClass = declaringClass;
         this.order = order;
         Class c = declaringClass;

         int i;
         for(i = 0; c.getSuperclass() != null; c = c.getSuperclass()) {
            ++i;
         }

         this.depth = i;
      } else {
         throw new IllegalArgumentException("fieldName or declaringClass is null");
      }
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public Class getDeclaringClass() {
      return this.declaringClass;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getOrder() {
      return this.order;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof FieldKey)) {
         return false;
      } else {
         FieldKey fieldKey = (FieldKey)o;
         if (!this.declaringClass.equals(fieldKey.declaringClass)) {
            return false;
         } else {
            return this.fieldName.equals(fieldKey.fieldName);
         }
      }
   }

   public int hashCode() {
      int result = this.fieldName.hashCode();
      result = 29 * result + this.declaringClass.hashCode();
      return result;
   }

   public String toString() {
      return "FieldKey{order=" + this.order + ", writer=" + this.depth + ", declaringClass=" + this.declaringClass + ", fieldName='" + this.fieldName + "'" + "}";
   }
}
