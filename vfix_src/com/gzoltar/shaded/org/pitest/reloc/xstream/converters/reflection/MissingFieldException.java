package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

public class MissingFieldException extends ObjectAccessException {
   private final String fieldName;
   private final String className;

   public MissingFieldException(String className, String fieldName) {
      super("No field '" + fieldName + "' found in class '" + className + "'");
      this.className = className;
      this.fieldName = fieldName;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   protected String getClassName() {
      return this.className;
   }
}
