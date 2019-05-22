package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ReferencingMarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.ArrayIterator;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastField;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Primitives;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.SerializationMembers;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.CannotResolveClassException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class AbstractReflectionConverter implements Converter, Caching {
   protected final ReflectionProvider reflectionProvider;
   protected final Mapper mapper;
   /** @deprecated */
   protected transient SerializationMethodInvoker serializationMethodInvoker;
   protected transient SerializationMembers serializationMembers;
   private transient ReflectionProvider pureJavaReflectionProvider;

   public AbstractReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
      this.mapper = mapper;
      this.reflectionProvider = reflectionProvider;
      this.serializationMethodInvoker = new SerializationMethodInvoker();
      this.serializationMembers = this.serializationMethodInvoker.serializationMembers;
   }

   protected boolean canAccess(Class type) {
      try {
         this.reflectionProvider.getFieldOrNull(type, "%");
         return true;
      } catch (NoClassDefFoundError var3) {
         return false;
      }
   }

   public void marshal(Object original, HierarchicalStreamWriter writer, MarshallingContext context) {
      Object source = this.serializationMembers.callWriteReplace(original);
      if (source != original && context instanceof ReferencingMarshallingContext) {
         ((ReferencingMarshallingContext)context).replace(original, source);
      }

      if (source.getClass() != original.getClass()) {
         String attributeName = this.mapper.aliasForSystemAttribute("resolves-to");
         if (attributeName != null) {
            writer.addAttribute(attributeName, this.mapper.serializedClass(source.getClass()));
         }

         context.convertAnother(source);
      } else {
         this.doMarshal(source, writer, context);
      }

   }

   protected void doMarshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
      final List fields = new ArrayList();
      final Map defaultFieldDefinition = new HashMap();
      this.reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {
         final Set writtenAttributes = new HashSet();

         public void visit(String fieldName, Class type, Class definedIn, Object value) {
            if (AbstractReflectionConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
               if (!defaultFieldDefinition.containsKey(fieldName)) {
                  Class lookupType = source.getClass();
                  if (definedIn != source.getClass() && !AbstractReflectionConverter.this.mapper.shouldSerializeMember(lookupType, fieldName)) {
                     lookupType = definedIn;
                  }

                  defaultFieldDefinition.put(fieldName, AbstractReflectionConverter.this.reflectionProvider.getField(lookupType, fieldName));
               }

               SingleValueConverter converter = AbstractReflectionConverter.this.mapper.getConverterFromItemType(fieldName, type, definedIn);
               if (converter != null) {
                  String attribute = AbstractReflectionConverter.this.mapper.aliasForAttribute(AbstractReflectionConverter.this.mapper.serializedMember(definedIn, fieldName));
                  if (value != null) {
                     if (this.writtenAttributes.contains(fieldName)) {
                        throw new ConversionException("Cannot write field with name '" + fieldName + "' twice as attribute for object of type " + source.getClass().getName());
                     }

                     String str = converter.toString(value);
                     if (str != null) {
                        writer.addAttribute(attribute, str);
                     }
                  }

                  this.writtenAttributes.add(fieldName);
               } else {
                  fields.add(new AbstractReflectionConverter.FieldInfo(fieldName, type, definedIn, value));
               }

            }
         }
      });
      Object var10001 = new Object() {
         {
            Iterator fieldIter = fields.iterator();

            while(true) {
               label61:
               while(true) {
                  AbstractReflectionConverter.FieldInfo info;
                  do {
                     if (!fieldIter.hasNext()) {
                        return;
                     }

                     info = (AbstractReflectionConverter.FieldInfo)fieldIter.next();
                  } while(info.value == null);

                  Mapper.ImplicitCollectionMapping mapping = AbstractReflectionConverter.this.mapper.getImplicitCollectionDefForFieldName(source.getClass(), info.fieldName);
                  if (mapping != null) {
                     if (context instanceof ReferencingMarshallingContext && info.value != Collections.EMPTY_LIST && info.value != Collections.EMPTY_SET && info.value != Collections.EMPTY_MAP) {
                        ReferencingMarshallingContext refContext = (ReferencingMarshallingContext)context;
                        refContext.registerImplicit(info.value);
                     }

                     boolean isCollection = info.value instanceof Collection;
                     boolean isMap = info.value instanceof Map;
                     boolean isEntry = isMap && mapping.getKeyFieldName() == null;
                     boolean isArray = info.value.getClass().isArray();
                     Object iter = isArray ? new ArrayIterator(info.value) : (isCollection ? ((Collection)info.value).iterator() : (isEntry ? ((Map)info.value).entrySet().iterator() : ((Map)info.value).values().iterator()));

                     while(true) {
                        while(true) {
                           if (!((Iterator)iter).hasNext()) {
                              continue label61;
                           }

                           Object obj = ((Iterator)iter).next();
                           Class itemType;
                           String itemName;
                           if (obj == null) {
                              itemType = Object.class;
                              itemName = AbstractReflectionConverter.this.mapper.serializedClass((Class)null);
                           } else {
                              if (isEntry) {
                                 String entryName = mapping.getItemFieldName() != null ? mapping.getItemFieldName() : AbstractReflectionConverter.this.mapper.serializedClass(Entry.class);
                                 Entry entry = (Entry)obj;
                                 ExtendedHierarchicalStreamWriterHelper.startNode(writer, entryName, entry.getClass());
                                 this.writeItem(entry.getKey(), context, writer);
                                 this.writeItem(entry.getValue(), context, writer);
                                 writer.endNode();
                                 continue;
                              }

                              if (mapping.getItemFieldName() != null) {
                                 itemType = mapping.getItemType();
                                 itemName = mapping.getItemFieldName();
                              } else {
                                 itemType = obj.getClass();
                                 itemName = AbstractReflectionConverter.this.mapper.serializedClass(itemType);
                              }
                           }

                           this.writeField(info.fieldName, itemName, itemType, info.definedIn, obj);
                        }
                     }
                  } else {
                     this.writeField(info.fieldName, (String)null, info.type, info.definedIn, info.value);
                  }
               }
            }
         }

         void writeField(String fieldName, String aliasName, Class fieldType, Class definedIn, Object newObj) {
            Class actualType = newObj != null ? newObj.getClass() : fieldType;
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, aliasName != null ? aliasName : AbstractReflectionConverter.this.mapper.serializedMember(source.getClass(), fieldName), actualType);
            if (newObj != null) {
               Class defaultType = AbstractReflectionConverter.this.mapper.defaultImplementationOf(fieldType);
               String attributeName;
               if (!actualType.equals(defaultType)) {
                  String serializedClassName = AbstractReflectionConverter.this.mapper.serializedClass(actualType);
                  if (!serializedClassName.equals(AbstractReflectionConverter.this.mapper.serializedClass(defaultType))) {
                     attributeName = AbstractReflectionConverter.this.mapper.aliasForSystemAttribute("class");
                     if (attributeName != null) {
                        writer.addAttribute(attributeName, serializedClassName);
                     }
                  }
               }

               Field defaultField = (Field)defaultFieldDefinition.get(fieldName);
               if (defaultField.getDeclaringClass() != definedIn) {
                  attributeName = AbstractReflectionConverter.this.mapper.aliasForSystemAttribute("defined-in");
                  if (attributeName != null) {
                     writer.addAttribute(attributeName, AbstractReflectionConverter.this.mapper.serializedClass(definedIn));
                  }
               }

               Field field = AbstractReflectionConverter.this.reflectionProvider.getField(definedIn, fieldName);
               AbstractReflectionConverter.this.marshallField(context, newObj, field);
            }

            writer.endNode();
         }

         void writeItem(Object item, MarshallingContext contextx, HierarchicalStreamWriter writerx) {
            String name;
            if (item == null) {
               name = AbstractReflectionConverter.this.mapper.serializedClass((Class)null);
               ExtendedHierarchicalStreamWriterHelper.startNode(writerx, name, Mapper.Null.class);
               writerx.endNode();
            } else {
               name = AbstractReflectionConverter.this.mapper.serializedClass(item.getClass());
               ExtendedHierarchicalStreamWriterHelper.startNode(writerx, name, item.getClass());
               contextx.convertAnother(item);
               writerx.endNode();
            }

         }
      };
   }

   protected void marshallField(MarshallingContext context, Object newObj, Field field) {
      context.convertAnother(newObj, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Object result = this.instantiateNewInstance(reader, context);
      result = this.doUnmarshal(result, reader, context);
      return this.serializationMembers.callReadResolve(result);
   }

   public Object doUnmarshal(Object result, HierarchicalStreamReader reader, UnmarshallingContext context) {
      Class resultType = result.getClass();
      Set seenFields = new HashSet() {
         public boolean add(Object e) {
            if (!super.add(e)) {
               throw new AbstractReflectionConverter.DuplicateFieldException(((FastField)e).getName());
            } else {
               return true;
            }
         }
      };
      Iterator it = reader.getAttributeNames();

      String originalNodeName;
      Class classDefiningField;
      Object value;
      while(it.hasNext()) {
         String attrAlias = (String)it.next();
         originalNodeName = this.mapper.realMember(resultType, this.mapper.attributeForAlias(attrAlias));
         Field field = this.reflectionProvider.getFieldOrNull(resultType, originalNodeName);
         if (field != null && this.shouldUnmarshalField(field)) {
            classDefiningField = field.getDeclaringClass();
            if (this.mapper.shouldSerializeMember(classDefiningField, originalNodeName)) {
               SingleValueConverter converter = this.mapper.getConverterFromAttribute(classDefiningField, originalNodeName, field.getType());
               Class type = field.getType();
               if (converter != null) {
                  value = converter.fromString(reader.getAttribute(attrAlias));
                  if (type.isPrimitive()) {
                     type = Primitives.box(type);
                  }

                  if (value != null && !type.isAssignableFrom(value.getClass())) {
                     throw new ConversionException("Cannot convert type " + value.getClass().getName() + " to type " + type.getName());
                  }

                  seenFields.add(new FastField(classDefiningField, originalNodeName));
                  this.reflectionProvider.writeField(result, originalNodeName, value, classDefiningField);
               }
            }
         }
      }

      HashMap implicitCollectionsForCurrentObject;
      for(implicitCollectionsForCurrentObject = null; reader.hasMoreChildren(); reader.moveUp()) {
         reader.moveDown();
         originalNodeName = reader.getNodeName();
         Class explicitDeclaringClass = this.readDeclaringClass(reader);
         classDefiningField = explicitDeclaringClass == null ? resultType : explicitDeclaringClass;
         String fieldName = this.mapper.realMember(classDefiningField, originalNodeName);
         Mapper.ImplicitCollectionMapping implicitCollectionMapping = this.mapper.getImplicitCollectionDefForFieldName(classDefiningField, fieldName);
         String implicitFieldName = null;
         Field field = null;
         Class type = null;
         if (implicitCollectionMapping == null) {
            field = this.reflectionProvider.getFieldOrNull(classDefiningField, fieldName);
            String classAttribute;
            if (field == null) {
               Class itemType = this.mapper.getItemTypeForItemFieldName(resultType, fieldName);
               if (itemType != null) {
                  classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
                  if (classAttribute != null) {
                     type = this.mapper.realClass(classAttribute);
                  } else {
                     type = itemType;
                  }
               } else {
                  try {
                     type = this.mapper.realClass(originalNodeName);
                     implicitFieldName = this.mapper.getFieldNameForItemTypeAndName(context.getRequiredType(), type, originalNodeName);
                  } catch (CannotResolveClassException var20) {
                  }

                  if (type == null || type != null && implicitFieldName == null) {
                     this.handleUnknownField(explicitDeclaringClass, fieldName, resultType, originalNodeName);
                     type = null;
                  }
               }

               if (type == null) {
                  value = null;
               } else if (Entry.class.equals(type)) {
                  reader.moveDown();
                  Object key = context.convertAnother(result, HierarchicalStreams.readClassType(reader, this.mapper));
                  reader.moveUp();
                  reader.moveDown();
                  Object v = context.convertAnother(result, HierarchicalStreams.readClassType(reader, this.mapper));
                  reader.moveUp();
                  value = Collections.singletonMap(key, v).entrySet().iterator().next();
               } else {
                  value = context.convertAnother(result, type);
               }
            } else {
               boolean fieldAlreadyChecked = false;
               if (explicitDeclaringClass == null) {
                  while(field != null && !(fieldAlreadyChecked = this.shouldUnmarshalField(field) && this.mapper.shouldSerializeMember(field.getDeclaringClass(), fieldName))) {
                     field = this.reflectionProvider.getFieldOrNull(field.getDeclaringClass().getSuperclass(), fieldName);
                  }
               }

               if (field == null || !fieldAlreadyChecked && (!this.shouldUnmarshalField(field) || !this.mapper.shouldSerializeMember(field.getDeclaringClass(), fieldName))) {
                  value = null;
               } else {
                  classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
                  if (classAttribute != null) {
                     type = this.mapper.realClass(classAttribute);
                  } else {
                     type = this.mapper.defaultImplementationOf(field.getType());
                  }

                  value = this.unmarshallField(context, result, type, field);
                  Class definedType = field.getType();
                  if (!definedType.isPrimitive()) {
                     type = definedType;
                  }
               }
            }
         } else {
            implicitFieldName = implicitCollectionMapping.getFieldName();
            type = implicitCollectionMapping.getItemType();
            if (type == null) {
               String classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
               type = this.mapper.realClass(classAttribute != null ? classAttribute : originalNodeName);
            }

            value = context.convertAnother(result, type);
         }

         if (value != null && !type.isAssignableFrom(value.getClass())) {
            throw new ConversionException("Cannot convert type " + value.getClass().getName() + " to type " + type.getName());
         }

         if (field != null) {
            this.reflectionProvider.writeField(result, fieldName, value, field.getDeclaringClass());
            seenFields.add(new FastField(field.getDeclaringClass(), fieldName));
         } else if (type != null) {
            if (implicitFieldName == null) {
               implicitFieldName = this.mapper.getFieldNameForItemTypeAndName(context.getRequiredType(), value != null ? value.getClass() : Mapper.Null.class, originalNodeName);
            }

            if (implicitCollectionsForCurrentObject == null) {
               implicitCollectionsForCurrentObject = new HashMap();
            }

            this.writeValueToImplicitCollection(value, implicitCollectionsForCurrentObject, result, implicitFieldName);
         }
      }

      if (implicitCollectionsForCurrentObject != null) {
         Iterator iter = implicitCollectionsForCurrentObject.entrySet().iterator();

         while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            Object value = entry.getValue();
            if (value instanceof AbstractReflectionConverter.ArraysList) {
               Object array = ((AbstractReflectionConverter.ArraysList)value).toPhysicalArray();
               this.reflectionProvider.writeField(result, (String)entry.getKey(), array, (Class)null);
            }
         }
      }

      return result;
   }

   protected Object unmarshallField(UnmarshallingContext context, Object result, Class type, Field field) {
      return context.convertAnother(result, type, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
   }

   protected boolean shouldUnmarshalTransientFields() {
      return false;
   }

   protected boolean shouldUnmarshalField(Field field) {
      return !Modifier.isTransient(field.getModifiers()) || this.shouldUnmarshalTransientFields();
   }

   private void handleUnknownField(Class classDefiningField, String fieldName, Class resultType, String originalNodeName) {
      if (classDefiningField == null) {
         for(Class cls = resultType; cls != null; cls = cls.getSuperclass()) {
            if (!this.mapper.shouldSerializeMember(cls, originalNodeName)) {
               return;
            }
         }
      }

      throw new AbstractReflectionConverter.UnknownFieldException(resultType.getName(), fieldName);
   }

   private void writeValueToImplicitCollection(Object value, Map implicitCollections, Object result, String implicitFieldName) {
      Collection collection = (Collection)implicitCollections.get(implicitFieldName);
      if (collection == null) {
         Class physicalFieldType = this.reflectionProvider.getFieldType(result, implicitFieldName, (Class)null);
         if (physicalFieldType.isArray()) {
            collection = new AbstractReflectionConverter.ArraysList(physicalFieldType);
         } else {
            Class fieldType = this.mapper.defaultImplementationOf(physicalFieldType);
            if (!Collection.class.isAssignableFrom(fieldType) && !Map.class.isAssignableFrom(fieldType)) {
               throw new ObjectAccessException("Field " + implicitFieldName + " of " + result.getClass().getName() + " is configured for an implicit Collection or Map, but field is of type " + fieldType.getName());
            }

            if (this.pureJavaReflectionProvider == null) {
               this.pureJavaReflectionProvider = new PureJavaReflectionProvider();
            }

            Object instance = this.pureJavaReflectionProvider.newInstance(fieldType);
            if (instance instanceof Collection) {
               collection = (Collection)instance;
            } else {
               Mapper.ImplicitCollectionMapping implicitCollectionMapping = this.mapper.getImplicitCollectionDefForFieldName(result.getClass(), implicitFieldName);
               collection = new AbstractReflectionConverter.MappingList((Map)instance, implicitCollectionMapping.getKeyFieldName());
            }

            this.reflectionProvider.writeField(result, implicitFieldName, instance, (Class)null);
         }

         implicitCollections.put(implicitFieldName, collection);
      }

      ((Collection)collection).add(value);
   }

   private Class readDeclaringClass(HierarchicalStreamReader reader) {
      String attributeName = this.mapper.aliasForSystemAttribute("defined-in");
      String definedIn = attributeName == null ? null : reader.getAttribute(attributeName);
      return definedIn == null ? null : this.mapper.realClass(definedIn);
   }

   protected Object instantiateNewInstance(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String attributeName = this.mapper.aliasForSystemAttribute("resolves-to");
      String readResolveValue = attributeName == null ? null : reader.getAttribute(attributeName);
      Object currentObject = context.currentObject();
      if (currentObject != null) {
         return currentObject;
      } else {
         return readResolveValue != null ? this.reflectionProvider.newInstance(this.mapper.realClass(readResolveValue)) : this.reflectionProvider.newInstance(context.getRequiredType());
      }
   }

   public void flushCache() {
      this.serializationMethodInvoker.flushCache();
   }

   protected Object readResolve() {
      this.serializationMethodInvoker = new SerializationMethodInvoker();
      this.serializationMembers = this.serializationMethodInvoker.serializationMembers;
      return this;
   }

   private class MappingList extends AbstractList {
      private final Map map;
      private final String keyFieldName;
      private final Map fieldCache = new HashMap();

      public MappingList(Map map, String keyFieldName) {
         this.map = map;
         this.keyFieldName = keyFieldName;
      }

      public boolean add(Object object) {
         if (object == null) {
            boolean containsNull = !this.map.containsKey((Object)null);
            this.map.put((Object)null, (Object)null);
            return containsNull;
         } else {
            Class itemType = object.getClass();
            if (this.keyFieldName != null) {
               Field field = (Field)this.fieldCache.get(itemType);
               if (field == null) {
                  field = AbstractReflectionConverter.this.reflectionProvider.getField(itemType, this.keyFieldName);
                  this.fieldCache.put(itemType, field);
               }

               if (field != null) {
                  try {
                     Object key = field.get(object);
                     return this.map.put(key, object) == null;
                  } catch (IllegalArgumentException var5) {
                     throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var5);
                  } catch (IllegalAccessException var6) {
                     throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var6);
                  }
               }
            } else if (object instanceof Entry) {
               Entry entry = (Entry)object;
               return this.map.put(entry.getKey(), entry.getValue()) == null;
            }

            throw new ConversionException("Element of type " + object.getClass().getName() + " is not defined as entry for map of type " + this.map.getClass().getName());
         }
      }

      public Object get(int index) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.map.size();
      }
   }

   private static class ArraysList extends ArrayList {
      final Class physicalFieldType;

      ArraysList(Class physicalFieldType) {
         this.physicalFieldType = physicalFieldType;
      }

      Object toPhysicalArray() {
         Object[] objects = this.toArray();
         Object array = Array.newInstance(this.physicalFieldType.getComponentType(), objects.length);
         if (this.physicalFieldType.getComponentType().isPrimitive()) {
            for(int i = 0; i < objects.length; ++i) {
               Array.set(array, i, Array.get(objects, i));
            }
         } else {
            System.arraycopy(objects, 0, array, 0, objects.length);
         }

         return array;
      }
   }

   private static class FieldInfo {
      final String fieldName;
      final Class type;
      final Class definedIn;
      final Object value;

      FieldInfo(String fieldName, Class type, Class definedIn, Object value) {
         this.fieldName = fieldName;
         this.type = type;
         this.definedIn = definedIn;
         this.value = value;
      }
   }

   public static class UnknownFieldException extends ConversionException {
      public UnknownFieldException(String type, String field) {
         super("No such field " + type + "." + field);
         this.add("field", field);
      }
   }

   public static class DuplicateFieldException extends ConversionException {
      public DuplicateFieldException(String msg) {
         super("Duplicate field " + msg);
         this.add("field", msg);
      }
   }
}
