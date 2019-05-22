package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Primitives;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClassAliasingMapper extends MapperWrapper {
   private final Map typeToName = new HashMap();
   private final Map classToName = new HashMap();
   private transient Map nameToType = new HashMap();

   public ClassAliasingMapper(Mapper wrapped) {
      super(wrapped);
   }

   public void addClassAlias(String name, Class type) {
      this.nameToType.put(name, type.getName());
      this.classToName.put(type.getName(), name);
   }

   /** @deprecated */
   public void addClassAttributeAlias(String name, Class type) {
      this.addClassAlias(name, type);
   }

   public void addTypeAlias(String name, Class type) {
      this.nameToType.put(name, type.getName());
      this.typeToName.put(type, name);
   }

   public String serializedClass(Class type) {
      String alias = (String)this.classToName.get(type.getName());
      if (alias != null) {
         return alias;
      } else {
         Iterator iter = this.typeToName.keySet().iterator();

         Class compatibleType;
         do {
            if (!iter.hasNext()) {
               return super.serializedClass(type);
            }

            compatibleType = (Class)iter.next();
         } while(!compatibleType.isAssignableFrom(type));

         return (String)this.typeToName.get(compatibleType);
      }
   }

   public Class realClass(String elementName) {
      String mappedName = (String)this.nameToType.get(elementName);
      if (mappedName != null) {
         Class type = Primitives.primitiveType(mappedName);
         if (type != null) {
            return type;
         }

         elementName = mappedName;
      }

      return super.realClass(elementName);
   }

   public boolean itemTypeAsAttribute(Class clazz) {
      return this.classToName.containsKey(clazz);
   }

   public boolean aliasIsAttribute(String name) {
      return this.nameToType.containsKey(name);
   }

   private Object readResolve() {
      this.nameToType = new HashMap();
      Iterator iter = this.classToName.keySet().iterator();

      while(iter.hasNext()) {
         Object type = iter.next();
         this.nameToType.put(this.classToName.get(type), type);
      }

      iter = this.typeToName.keySet().iterator();

      while(iter.hasNext()) {
         Class type = (Class)iter.next();
         this.nameToType.put(this.typeToName.get(type), type.getName());
      }

      return this;
   }
}
