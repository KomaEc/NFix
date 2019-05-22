package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Primitives;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ImplicitCollectionMapper extends MapperWrapper {
   private final Map classNameToMapper = new HashMap();

   public ImplicitCollectionMapper(Mapper wrapped) {
      super(wrapped);
   }

   private ImplicitCollectionMapper.ImplicitCollectionMapperForClass getMapper(Class definedIn) {
      while(definedIn != null) {
         ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = (ImplicitCollectionMapper.ImplicitCollectionMapperForClass)this.classNameToMapper.get(definedIn);
         if (mapper != null) {
            return mapper;
         }

         definedIn = definedIn.getSuperclass();
      }

      return null;
   }

   private ImplicitCollectionMapper.ImplicitCollectionMapperForClass getOrCreateMapper(Class definedIn) {
      ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = (ImplicitCollectionMapper.ImplicitCollectionMapperForClass)this.classNameToMapper.get(definedIn);
      if (mapper == null) {
         mapper = new ImplicitCollectionMapper.ImplicitCollectionMapperForClass(definedIn);
         this.classNameToMapper.put(definedIn, mapper);
      }

      return mapper;
   }

   public String getFieldNameForItemTypeAndName(Class definedIn, Class itemType, String itemFieldName) {
      ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = this.getMapper(definedIn);
      return mapper != null ? mapper.getFieldNameForItemTypeAndName(itemType, itemFieldName) : null;
   }

   public Class getItemTypeForItemFieldName(Class definedIn, String itemFieldName) {
      ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = this.getMapper(definedIn);
      return mapper != null ? mapper.getItemTypeForItemFieldName(itemFieldName) : null;
   }

   public Mapper.ImplicitCollectionMapping getImplicitCollectionDefForFieldName(Class itemType, String fieldName) {
      ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = this.getMapper(itemType);
      return mapper != null ? mapper.getImplicitCollectionDefForFieldName(fieldName) : null;
   }

   public void add(Class definedIn, String fieldName, Class itemType) {
      this.add(definedIn, fieldName, (String)null, itemType);
   }

   public void add(Class definedIn, String fieldName, String itemFieldName, Class itemType) {
      this.add(definedIn, fieldName, itemFieldName, itemType, (String)null);
   }

   public void add(Class definedIn, String fieldName, String itemFieldName, Class itemType, String keyFieldName) {
      Field field = null;
      Class declaredIn = definedIn;

      while(declaredIn != Object.class && definedIn != null) {
         try {
            field = declaredIn.getDeclaredField(fieldName);
            break;
         } catch (SecurityException var10) {
            throw new InitializationException("Access denied for field with implicit collection", var10);
         } catch (NoSuchFieldException var11) {
            declaredIn = declaredIn.getSuperclass();
         }
      }

      if (field == null) {
         throw new InitializationException("No field \"" + fieldName + "\" for implicit collection");
      } else {
         if (Map.class.isAssignableFrom(field.getType())) {
            if (itemFieldName == null && keyFieldName == null) {
               itemType = Entry.class;
            }
         } else if (!Collection.class.isAssignableFrom(field.getType())) {
            Class fieldType = field.getType();
            if (!fieldType.isArray()) {
               throw new InitializationException("Field \"" + fieldName + "\" declares no collection or array");
            }

            Class componentType = fieldType.getComponentType();
            componentType = componentType.isPrimitive() ? Primitives.box(componentType) : componentType;
            if (itemType == null) {
               itemType = componentType;
            } else {
               itemType = itemType.isPrimitive() ? Primitives.box(itemType) : itemType;
               if (!componentType.isAssignableFrom(itemType)) {
                  throw new InitializationException("Field \"" + fieldName + "\" declares an array, but the array type is not compatible with " + itemType.getName());
               }
            }
         }

         ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = this.getOrCreateMapper(definedIn);
         mapper.add(new ImplicitCollectionMapper.ImplicitCollectionMappingImpl(fieldName, itemType, itemFieldName, keyFieldName));
      }
   }

   private static class NamedItemType {
      Class itemType;
      String itemFieldName;

      NamedItemType(Class itemType, String itemFieldName) {
         this.itemType = itemType == null ? Object.class : itemType;
         this.itemFieldName = itemFieldName;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ImplicitCollectionMapper.NamedItemType)) {
            return false;
         } else {
            ImplicitCollectionMapper.NamedItemType b = (ImplicitCollectionMapper.NamedItemType)obj;
            return this.itemType.equals(b.itemType) && isEquals(this.itemFieldName, b.itemFieldName);
         }
      }

      private static boolean isEquals(Object a, Object b) {
         if (a == null) {
            return b == null;
         } else {
            return a.equals(b);
         }
      }

      public int hashCode() {
         int hash = this.itemType.hashCode() << 7;
         if (this.itemFieldName != null) {
            hash += this.itemFieldName.hashCode();
         }

         return hash;
      }
   }

   private static class ImplicitCollectionMappingImpl implements Mapper.ImplicitCollectionMapping {
      private final String fieldName;
      private final String itemFieldName;
      private final Class itemType;
      private final String keyFieldName;

      ImplicitCollectionMappingImpl(String fieldName, Class itemType, String itemFieldName, String keyFieldName) {
         this.fieldName = fieldName;
         this.itemFieldName = itemFieldName;
         this.itemType = itemType;
         this.keyFieldName = keyFieldName;
      }

      public ImplicitCollectionMapper.NamedItemType createNamedItemType() {
         return new ImplicitCollectionMapper.NamedItemType(this.itemType, this.itemFieldName);
      }

      public String getFieldName() {
         return this.fieldName;
      }

      public String getItemFieldName() {
         return this.itemFieldName;
      }

      public Class getItemType() {
         return this.itemType;
      }

      public String getKeyFieldName() {
         return this.keyFieldName;
      }
   }

   private class ImplicitCollectionMapperForClass {
      private Class definedIn;
      private Map namedItemTypeToDef = new HashMap();
      private Map itemFieldNameToDef = new HashMap();
      private Map fieldNameToDef = new HashMap();

      ImplicitCollectionMapperForClass(Class definedIn) {
         this.definedIn = definedIn;
      }

      public String getFieldNameForItemTypeAndName(Class itemType, String itemFieldName) {
         ImplicitCollectionMapper.ImplicitCollectionMappingImpl unnamed = null;
         Iterator iterator = this.namedItemTypeToDef.keySet().iterator();

         while(iterator.hasNext()) {
            ImplicitCollectionMapper.NamedItemType itemTypeForFieldName = (ImplicitCollectionMapper.NamedItemType)iterator.next();
            ImplicitCollectionMapper.ImplicitCollectionMappingImpl def = (ImplicitCollectionMapper.ImplicitCollectionMappingImpl)this.namedItemTypeToDef.get(itemTypeForFieldName);
            if (itemType == Mapper.Null.class) {
               unnamed = def;
               break;
            }

            if (itemTypeForFieldName.itemType.isAssignableFrom(itemType)) {
               if (def.getItemFieldName() != null) {
                  if (def.getItemFieldName().equals(itemFieldName)) {
                     return def.getFieldName();
                  }
               } else if (unnamed == null || unnamed.getItemType() == null || def.getItemType() != null && unnamed.getItemType().isAssignableFrom(def.getItemType())) {
                  unnamed = def;
               }
            }
         }

         if (unnamed != null) {
            return unnamed.getFieldName();
         } else {
            ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = ImplicitCollectionMapper.this.getMapper(this.definedIn.getSuperclass());
            return mapper != null ? mapper.getFieldNameForItemTypeAndName(itemType, itemFieldName) : null;
         }
      }

      public Class getItemTypeForItemFieldName(String itemFieldName) {
         ImplicitCollectionMapper.ImplicitCollectionMappingImpl def = this.getImplicitCollectionDefByItemFieldName(itemFieldName);
         if (def != null) {
            return def.getItemType();
         } else {
            ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = ImplicitCollectionMapper.this.getMapper(this.definedIn.getSuperclass());
            return mapper != null ? mapper.getItemTypeForItemFieldName(itemFieldName) : null;
         }
      }

      private ImplicitCollectionMapper.ImplicitCollectionMappingImpl getImplicitCollectionDefByItemFieldName(String itemFieldName) {
         if (itemFieldName == null) {
            return null;
         } else {
            ImplicitCollectionMapper.ImplicitCollectionMappingImpl mapping = (ImplicitCollectionMapper.ImplicitCollectionMappingImpl)this.itemFieldNameToDef.get(itemFieldName);
            if (mapping != null) {
               return mapping;
            } else {
               ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = ImplicitCollectionMapper.this.getMapper(this.definedIn.getSuperclass());
               return mapper != null ? mapper.getImplicitCollectionDefByItemFieldName(itemFieldName) : null;
            }
         }
      }

      public Mapper.ImplicitCollectionMapping getImplicitCollectionDefForFieldName(String fieldName) {
         Mapper.ImplicitCollectionMapping mapping = (Mapper.ImplicitCollectionMapping)this.fieldNameToDef.get(fieldName);
         if (mapping != null) {
            return mapping;
         } else {
            ImplicitCollectionMapper.ImplicitCollectionMapperForClass mapper = ImplicitCollectionMapper.this.getMapper(this.definedIn.getSuperclass());
            return mapper != null ? mapper.getImplicitCollectionDefForFieldName(fieldName) : null;
         }
      }

      public void add(ImplicitCollectionMapper.ImplicitCollectionMappingImpl def) {
         this.fieldNameToDef.put(def.getFieldName(), def);
         this.namedItemTypeToDef.put(def.createNamedItemType(), def);
         if (def.getItemFieldName() != null) {
            this.itemFieldNameToDef.put(def.getItemFieldName(), def);
         }

      }
   }
}
