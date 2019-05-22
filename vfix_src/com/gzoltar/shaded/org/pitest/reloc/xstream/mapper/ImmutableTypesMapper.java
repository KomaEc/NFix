package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import java.util.HashSet;
import java.util.Set;

public class ImmutableTypesMapper extends MapperWrapper {
   private final Set immutableTypes = new HashSet();

   public ImmutableTypesMapper(Mapper wrapped) {
      super(wrapped);
   }

   public void addImmutableType(Class type) {
      this.immutableTypes.add(type);
   }

   public boolean isImmutableValueType(Class type) {
      return this.immutableTypes.contains(type) ? true : super.isImmutableValueType(type);
   }
}
