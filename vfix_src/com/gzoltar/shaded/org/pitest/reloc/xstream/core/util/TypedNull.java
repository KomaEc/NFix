package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

public class TypedNull {
   private final Class type;

   public TypedNull(Class type) {
      this.type = type;
   }

   public Class getType() {
      return this.type;
   }
}
