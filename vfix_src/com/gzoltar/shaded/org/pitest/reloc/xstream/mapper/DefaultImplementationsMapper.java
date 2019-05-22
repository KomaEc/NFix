package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultImplementationsMapper extends MapperWrapper {
   private final Map typeToImpl = new HashMap();
   private transient Map implToType = new HashMap();

   public DefaultImplementationsMapper(Mapper wrapped) {
      super(wrapped);
      this.addDefaults();
   }

   protected void addDefaults() {
      this.addDefaultImplementation((Class)null, Mapper.Null.class);
      this.addDefaultImplementation(Boolean.class, Boolean.TYPE);
      this.addDefaultImplementation(Character.class, Character.TYPE);
      this.addDefaultImplementation(Integer.class, Integer.TYPE);
      this.addDefaultImplementation(Float.class, Float.TYPE);
      this.addDefaultImplementation(Double.class, Double.TYPE);
      this.addDefaultImplementation(Short.class, Short.TYPE);
      this.addDefaultImplementation(Byte.class, Byte.TYPE);
      this.addDefaultImplementation(Long.class, Long.TYPE);
   }

   public void addDefaultImplementation(Class defaultImplementation, Class ofType) {
      if (defaultImplementation != null && defaultImplementation.isInterface()) {
         throw new InitializationException("Default implementation is not a concrete class: " + defaultImplementation.getName());
      } else {
         this.typeToImpl.put(ofType, defaultImplementation);
         this.implToType.put(defaultImplementation, ofType);
      }
   }

   public String serializedClass(Class type) {
      Class baseType = (Class)this.implToType.get(type);
      return baseType == null ? super.serializedClass(type) : super.serializedClass(baseType);
   }

   public Class defaultImplementationOf(Class type) {
      return this.typeToImpl.containsKey(type) ? (Class)this.typeToImpl.get(type) : super.defaultImplementationOf(type);
   }

   private Object readResolve() {
      this.implToType = new HashMap();
      Iterator iter = this.typeToImpl.keySet().iterator();

      while(iter.hasNext()) {
         Object type = iter.next();
         this.implToType.put(this.typeToImpl.get(type), type);
      }

      return this;
   }
}
