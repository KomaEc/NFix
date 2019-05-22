package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectInputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectOutputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SerializableConverter extends AbstractReflectionConverter {
   private static final String ELEMENT_NULL = "null";
   private static final String ELEMENT_DEFAULT = "default";
   private static final String ELEMENT_UNSERIALIZABLE_PARENTS = "unserializable-parents";
   private static final String ATTRIBUTE_CLASS = "class";
   private static final String ATTRIBUTE_SERIALIZATION = "serialization";
   private static final String ATTRIBUTE_VALUE_CUSTOM = "custom";
   private static final String ELEMENT_FIELDS = "fields";
   private static final String ELEMENT_FIELD = "field";
   private static final String ATTRIBUTE_NAME = "name";
   private final ClassLoaderReference classLoaderReference;

   public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoaderReference classLoaderReference) {
      super(mapper, new SerializableConverter.UnserializableParentsReflectionProvider(reflectionProvider));
      this.classLoaderReference = classLoaderReference;
   }

   /** @deprecated */
   public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoader classLoader) {
      this(mapper, reflectionProvider, new ClassLoaderReference(classLoader));
   }

   /** @deprecated */
   public SerializableConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
      this(mapper, new SerializableConverter.UnserializableParentsReflectionProvider(reflectionProvider), (ClassLoaderReference)(new ClassLoaderReference((ClassLoader)null)));
   }

   public boolean canConvert(Class type) {
      return JVM.canCreateDerivedObjectOutputStream() && this.isSerializable(type);
   }

   private boolean isSerializable(Class type) {
      if (type == null || !Serializable.class.isAssignableFrom(type) || type.isInterface() || !this.serializationMembers.supportsReadObject(type, true) && !this.serializationMembers.supportsWriteObject(type, true)) {
         return false;
      } else {
         Iterator iter = this.hierarchyFor(type).iterator();

         do {
            if (!iter.hasNext()) {
               return true;
            }
         } while(Serializable.class.isAssignableFrom((Class)iter.next()));

         return this.canAccess(type);
      }
   }

   public void doMarshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
      String attributeName = this.mapper.aliasForSystemAttribute("serialization");
      if (attributeName != null) {
         writer.addAttribute(attributeName, "custom");
      }

      final Class[] currentType = new Class[1];
      final boolean[] writtenClassWrapper = new boolean[]{false};
      CustomObjectOutputStream.StreamCallback callback = new CustomObjectOutputStream.StreamCallback() {
         public void writeToStream(Object object) {
            if (object == null) {
               writer.startNode("null");
               writer.endNode();
            } else {
               ExtendedHierarchicalStreamWriterHelper.startNode(writer, SerializableConverter.this.mapper.serializedClass(object.getClass()), object.getClass());
               context.convertAnother(object);
               writer.endNode();
            }

         }

         public void writeFieldsToStream(Map fields) {
            ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(currentType[0]);
            writer.startNode("default");
            Iterator iterator = fields.keySet().iterator();

            while(iterator.hasNext()) {
               String name = (String)iterator.next();
               if (SerializableConverter.this.mapper.shouldSerializeMember(currentType[0], name)) {
                  ObjectStreamField field = objectStreamClass.getField(name);
                  Object value = fields.get(name);
                  if (field == null) {
                     throw new ObjectAccessException("Class " + value.getClass().getName() + " may not write a field named '" + name + "'");
                  }

                  if (value != null) {
                     ExtendedHierarchicalStreamWriterHelper.startNode(writer, SerializableConverter.this.mapper.serializedMember(source.getClass(), name), value.getClass());
                     if (field.getType() != value.getClass() && !field.getType().isPrimitive()) {
                        String attributeName = SerializableConverter.this.mapper.aliasForSystemAttribute("class");
                        if (attributeName != null) {
                           writer.addAttribute(attributeName, SerializableConverter.this.mapper.serializedClass(value.getClass()));
                        }
                     }

                     context.convertAnother(value);
                     writer.endNode();
                  }
               }
            }

            writer.endNode();
         }

         public void defaultWriteObject() {
            boolean writtenDefaultFields = false;
            ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(currentType[0]);
            if (objectStreamClass != null) {
               ObjectStreamField[] fields = objectStreamClass.getFields();

               for(int i = 0; i < fields.length; ++i) {
                  ObjectStreamField field = fields[i];
                  Object value = SerializableConverter.this.readField(field, currentType[0], source);
                  if (value != null) {
                     if (!writtenClassWrapper[0]) {
                        writer.startNode(SerializableConverter.this.mapper.serializedClass(currentType[0]));
                        writtenClassWrapper[0] = true;
                     }

                     if (!writtenDefaultFields) {
                        writer.startNode("default");
                        writtenDefaultFields = true;
                     }

                     if (SerializableConverter.this.mapper.shouldSerializeMember(currentType[0], field.getName())) {
                        Class actualType = value.getClass();
                        ExtendedHierarchicalStreamWriterHelper.startNode(writer, SerializableConverter.this.mapper.serializedMember(source.getClass(), field.getName()), actualType);
                        Class defaultType = SerializableConverter.this.mapper.defaultImplementationOf(field.getType());
                        if (!actualType.equals(defaultType)) {
                           String attributeName = SerializableConverter.this.mapper.aliasForSystemAttribute("class");
                           if (attributeName != null) {
                              writer.addAttribute(attributeName, SerializableConverter.this.mapper.serializedClass(actualType));
                           }
                        }

                        context.convertAnother(value);
                        writer.endNode();
                     }
                  }
               }

               if (writtenClassWrapper[0] && !writtenDefaultFields) {
                  writer.startNode("default");
                  writer.endNode();
               } else if (writtenDefaultFields) {
                  writer.endNode();
               }

            }
         }

         public void flush() {
            writer.flush();
         }

         public void close() {
            throw new UnsupportedOperationException("Objects are not allowed to call ObjectOutputStream.close() from writeObject()");
         }
      };

      try {
         boolean mustHandleUnserializableParent = false;
         Iterator classHieararchy = this.hierarchyFor(source.getClass()).iterator();

         while(classHieararchy.hasNext()) {
            currentType[0] = (Class)classHieararchy.next();
            if (!Serializable.class.isAssignableFrom(currentType[0])) {
               mustHandleUnserializableParent = true;
            } else {
               if (mustHandleUnserializableParent) {
                  this.marshalUnserializableParent(writer, context, source);
                  mustHandleUnserializableParent = false;
               }

               String classAttributeName;
               if (this.serializationMembers.supportsWriteObject(currentType[0], false)) {
                  writtenClassWrapper[0] = true;
                  writer.startNode(this.mapper.serializedClass(currentType[0]));
                  if (currentType[0] != this.mapper.defaultImplementationOf(currentType[0])) {
                     classAttributeName = this.mapper.aliasForSystemAttribute("class");
                     if (classAttributeName != null) {
                        writer.addAttribute(classAttributeName, currentType[0].getName());
                     }
                  }

                  CustomObjectOutputStream objectOutputStream = CustomObjectOutputStream.getInstance(context, callback);
                  this.serializationMembers.callWriteObject(currentType[0], source, objectOutputStream);
                  objectOutputStream.popCallback();
                  writer.endNode();
               } else if (this.serializationMembers.supportsReadObject(currentType[0], false)) {
                  writtenClassWrapper[0] = true;
                  writer.startNode(this.mapper.serializedClass(currentType[0]));
                  if (currentType[0] != this.mapper.defaultImplementationOf(currentType[0])) {
                     classAttributeName = this.mapper.aliasForSystemAttribute("class");
                     if (classAttributeName != null) {
                        writer.addAttribute(classAttributeName, currentType[0].getName());
                     }
                  }

                  callback.defaultWriteObject();
                  writer.endNode();
               } else {
                  writtenClassWrapper[0] = false;
                  callback.defaultWriteObject();
                  if (writtenClassWrapper[0]) {
                     writer.endNode();
                  }
               }
            }
         }

      } catch (IOException var11) {
         throw new ObjectAccessException("Could not call defaultWriteObject()", var11);
      }
   }

   protected void marshalUnserializableParent(HierarchicalStreamWriter writer, MarshallingContext context, Object replacedSource) {
      writer.startNode("unserializable-parents");
      super.doMarshal(replacedSource, writer, context);
      writer.endNode();
   }

   private Object readField(ObjectStreamField field, Class type, Object instance) {
      try {
         Field javaField = type.getDeclaredField(field.getName());
         if (!javaField.isAccessible()) {
            javaField.setAccessible(true);
         }

         return javaField.get(instance);
      } catch (IllegalArgumentException var5) {
         throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var5);
      } catch (IllegalAccessException var6) {
         throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var6);
      } catch (NoSuchFieldException var7) {
         throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var7);
      } catch (SecurityException var8) {
         throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var8);
      }
   }

   protected List hierarchyFor(Class type) {
      ArrayList result;
      for(result = new ArrayList(); type != Object.class && type != null; type = type.getSuperclass()) {
         result.add(type);
      }

      Collections.reverse(result);
      return result;
   }

   public Object doUnmarshal(final Object result, final HierarchicalStreamReader reader, final UnmarshallingContext context) {
      final Class[] currentType = new Class[1];
      String attributeName = this.mapper.aliasForSystemAttribute("serialization");
      if (attributeName != null && !"custom".equals(reader.getAttribute(attributeName))) {
         throw new ConversionException("Cannot deserialize object with new readObject()/writeObject() methods");
      } else {
         for(CustomObjectInputStream.StreamCallback callback = new CustomObjectInputStream.StreamCallback() {
            public Object readFromStream() {
               reader.moveDown();
               Class type = HierarchicalStreams.readClassType(reader, SerializableConverter.this.mapper);
               Object value = context.convertAnother(result, type);
               reader.moveUp();
               return value;
            }

            public Map readFieldsFromStream() {
               Map fields = new HashMap();
               reader.moveDown();
               if (reader.getNodeName().equals("fields")) {
                  while(reader.hasMoreChildren()) {
                     reader.moveDown();
                     if (!reader.getNodeName().equals("field")) {
                        throw new ConversionException("Expected <field/> element inside <field/>");
                     }

                     String name = reader.getAttribute("name");
                     Class type = SerializableConverter.this.mapper.realClass(reader.getAttribute("class"));
                     Object value = context.convertAnother(result, type);
                     fields.put(name, value);
                     reader.moveUp();
                  }
               } else {
                  if (!reader.getNodeName().equals("default")) {
                     throw new ConversionException("Expected <fields/> or <default/> element when calling ObjectInputStream.readFields()");
                  }

                  for(ObjectStreamClass objectStreamClass = ObjectStreamClass.lookup(currentType[0]); reader.hasMoreChildren(); reader.moveUp()) {
                     reader.moveDown();
                     String namex = SerializableConverter.this.mapper.realMember(currentType[0], reader.getNodeName());
                     if (SerializableConverter.this.mapper.shouldSerializeMember(currentType[0], namex)) {
                        String classAttribute = HierarchicalStreams.readClassAttribute(reader, SerializableConverter.this.mapper);
                        Class typex;
                        if (classAttribute != null) {
                           typex = SerializableConverter.this.mapper.realClass(classAttribute);
                        } else {
                           ObjectStreamField field = objectStreamClass.getField(namex);
                           if (field == null) {
                              throw new MissingFieldException(currentType[0].getName(), namex);
                           }

                           typex = field.getType();
                        }

                        Object valuex = context.convertAnother(result, typex);
                        fields.put(namex, valuex);
                     }
                  }
               }

               reader.moveUp();
               return fields;
            }

            public void defaultReadObject() {
               if (SerializableConverter.this.serializationMembers.getSerializablePersistentFields(currentType[0]) != null) {
                  this.readFieldsFromStream();
               } else if (reader.hasMoreChildren()) {
                  reader.moveDown();
                  if (!reader.getNodeName().equals("default")) {
                     throw new ConversionException("Expected <default/> element in readObject() stream");
                  } else {
                     for(; reader.hasMoreChildren(); reader.moveUp()) {
                        reader.moveDown();
                        String fieldName = SerializableConverter.this.mapper.realMember(currentType[0], reader.getNodeName());
                        if (SerializableConverter.this.mapper.shouldSerializeMember(currentType[0], fieldName)) {
                           String classAttribute = HierarchicalStreams.readClassAttribute(reader, SerializableConverter.this.mapper);
                           Class type;
                           if (classAttribute != null) {
                              type = SerializableConverter.this.mapper.realClass(classAttribute);
                           } else {
                              type = SerializableConverter.this.mapper.defaultImplementationOf(SerializableConverter.this.reflectionProvider.getFieldType(result, fieldName, currentType[0]));
                           }

                           Object value = context.convertAnother(result, type);
                           SerializableConverter.this.reflectionProvider.writeField(result, fieldName, value, currentType[0]);
                        }
                     }

                     reader.moveUp();
                  }
               }
            }

            public void registerValidation(final ObjectInputValidation validation, int priority) {
               context.addCompletionCallback(new Runnable() {
                  public void run() {
                     try {
                        validation.validateObject();
                     } catch (InvalidObjectException var2) {
                        throw new ObjectAccessException("Cannot validate object : " + var2.getMessage(), var2);
                     }
                  }
               }, priority);
            }

            public void close() {
               throw new UnsupportedOperationException("Objects are not allowed to call ObjectInputStream.close() from readObject()");
            }
         }; reader.hasMoreChildren(); reader.moveUp()) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if (nodeName.equals("unserializable-parents")) {
               super.doUnmarshal(result, reader, context);
            } else {
               String classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
               if (classAttribute == null) {
                  currentType[0] = this.mapper.defaultImplementationOf(this.mapper.realClass(nodeName));
               } else {
                  currentType[0] = this.mapper.realClass(classAttribute);
               }

               if (this.serializationMembers.supportsReadObject(currentType[0], false)) {
                  CustomObjectInputStream objectInputStream = CustomObjectInputStream.getInstance(context, callback, (ClassLoaderReference)this.classLoaderReference);
                  this.serializationMembers.callReadObject(currentType[0], result, objectInputStream);
                  objectInputStream.popCallback();
               } else {
                  try {
                     callback.defaultReadObject();
                  } catch (IOException var10) {
                     throw new ObjectAccessException("Could not call defaultWriteObject()", var10);
                  }
               }
            }
         }

         return result;
      }
   }

   protected void doMarshalConditionally(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      if (this.isSerializable(source.getClass())) {
         this.doMarshal(source, writer, context);
      } else {
         super.doMarshal(source, writer, context);
      }

   }

   protected Object doUnmarshalConditionally(Object result, HierarchicalStreamReader reader, UnmarshallingContext context) {
      return this.isSerializable(result.getClass()) ? this.doUnmarshal(result, reader, context) : super.doUnmarshal(result, reader, context);
   }

   private static class UnserializableParentsReflectionProvider extends ReflectionProviderWrapper {
      public UnserializableParentsReflectionProvider(ReflectionProvider reflectionProvider) {
         super(reflectionProvider);
      }

      public void visitSerializableFields(Object object, final ReflectionProvider.Visitor visitor) {
         this.wrapped.visitSerializableFields(object, new ReflectionProvider.Visitor() {
            public void visit(String name, Class type, Class definedIn, Object value) {
               if (!Serializable.class.isAssignableFrom(definedIn)) {
                  visitor.visit(name, type, definedIn, value);
               }

            }
         });
      }
   }
}
