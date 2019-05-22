package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class PureJavaReflectionProvider implements ReflectionProvider {
   private transient Map serializedDataCache;
   protected FieldDictionary fieldDictionary;

   public PureJavaReflectionProvider() {
      this(new FieldDictionary(new ImmutableFieldKeySorter()));
   }

   public PureJavaReflectionProvider(FieldDictionary fieldDictionary) {
      this.fieldDictionary = fieldDictionary;
      this.init();
   }

   public Object newInstance(Class type) {
      try {
         Constructor[] constructors = type.getDeclaredConstructors();

         for(int i = 0; i < constructors.length; ++i) {
            Constructor constructor = constructors[i];
            if (constructor.getParameterTypes().length == 0) {
               if (!constructor.isAccessible()) {
                  constructor.setAccessible(true);
               }

               return constructor.newInstance();
            }
         }

         if (Serializable.class.isAssignableFrom(type)) {
            return this.instantiateUsingSerialization(type);
         } else {
            throw new ObjectAccessException("Cannot construct " + type.getName() + " as it does not have a no-args constructor");
         }
      } catch (InstantiationException var5) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var5);
      } catch (IllegalAccessException var6) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var6);
      } catch (InvocationTargetException var7) {
         if (var7.getTargetException() instanceof RuntimeException) {
            throw (RuntimeException)var7.getTargetException();
         } else if (var7.getTargetException() instanceof Error) {
            throw (Error)var7.getTargetException();
         } else {
            throw new ObjectAccessException("Constructor for " + type.getName() + " threw an exception", var7.getTargetException());
         }
      }
   }

   private Object instantiateUsingSerialization(final Class type) {
      try {
         synchronized(this.serializedDataCache) {
            byte[] data = (byte[])((byte[])this.serializedDataCache.get(type));
            if (data == null) {
               ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               DataOutputStream stream = new DataOutputStream(bytes);
               stream.writeShort(-21267);
               stream.writeShort(5);
               stream.writeByte(115);
               stream.writeByte(114);
               stream.writeUTF(type.getName());
               stream.writeLong(ObjectStreamClass.lookup(type).getSerialVersionUID());
               stream.writeByte(2);
               stream.writeShort(0);
               stream.writeByte(120);
               stream.writeByte(112);
               data = bytes.toByteArray();
               this.serializedDataCache.put(type, data);
            }

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data)) {
               protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                  return Class.forName(desc.getName(), false, type.getClassLoader());
               }
            };
            return in.readObject();
         }
      } catch (IOException var8) {
         throw new ObjectAccessException("Cannot create " + type.getName() + " by JDK serialization", var8);
      } catch (ClassNotFoundException var9) {
         throw new ObjectAccessException("Cannot find class " + var9.getMessage(), var9);
      }
   }

   public void visitSerializableFields(Object object, ReflectionProvider.Visitor visitor) {
      Iterator iterator = this.fieldDictionary.fieldsFor(object.getClass());

      while(iterator.hasNext()) {
         Field field = (Field)iterator.next();
         if (this.fieldModifiersSupported(field)) {
            this.validateFieldAccess(field);

            try {
               Object value = field.get(object);
               visitor.visit(field.getName(), field.getType(), field.getDeclaringClass(), value);
            } catch (IllegalArgumentException var6) {
               throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var6);
            } catch (IllegalAccessException var7) {
               throw new ObjectAccessException("Could not get field " + field.getClass() + "." + field.getName(), var7);
            }
         }
      }

   }

   public void writeField(Object object, String fieldName, Object value, Class definedIn) {
      Field field = this.fieldDictionary.field(object.getClass(), fieldName, definedIn);
      this.validateFieldAccess(field);

      try {
         field.set(object, value);
      } catch (IllegalArgumentException var7) {
         throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), var7);
      } catch (IllegalAccessException var8) {
         throw new ObjectAccessException("Could not set field " + object.getClass() + "." + field.getName(), var8);
      }
   }

   public Class getFieldType(Object object, String fieldName, Class definedIn) {
      return this.fieldDictionary.field(object.getClass(), fieldName, definedIn).getType();
   }

   /** @deprecated */
   public boolean fieldDefinedInClass(String fieldName, Class type) {
      Field field = this.fieldDictionary.fieldOrNull(type, fieldName, (Class)null);
      return field != null && this.fieldModifiersSupported(field);
   }

   protected boolean fieldModifiersSupported(Field field) {
      int modifiers = field.getModifiers();
      return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
   }

   protected void validateFieldAccess(Field field) {
      if (Modifier.isFinal(field.getModifiers())) {
         if (!JVM.is15()) {
            throw new ObjectAccessException("Invalid final field " + field.getDeclaringClass().getName() + "." + field.getName());
         }

         if (!field.isAccessible()) {
            field.setAccessible(true);
         }
      }

   }

   public Field getField(Class definedIn, String fieldName) {
      return this.fieldDictionary.field(definedIn, fieldName, (Class)null);
   }

   public Field getFieldOrNull(Class definedIn, String fieldName) {
      return this.fieldDictionary.fieldOrNull(definedIn, fieldName, (Class)null);
   }

   public void setFieldDictionary(FieldDictionary dictionary) {
      this.fieldDictionary = dictionary;
   }

   private Object readResolve() {
      this.init();
      return this;
   }

   protected void init() {
      this.serializedDataCache = new WeakHashMap();
   }
}
