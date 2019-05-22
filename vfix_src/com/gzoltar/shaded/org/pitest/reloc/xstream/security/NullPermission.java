package com.gzoltar.shaded.org.pitest.reloc.xstream.security;

import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class NullPermission implements TypePermission {
   public static final TypePermission NULL = new NullPermission();

   public boolean allows(Class type) {
      return type == null || type == Mapper.Null.class;
   }
}
