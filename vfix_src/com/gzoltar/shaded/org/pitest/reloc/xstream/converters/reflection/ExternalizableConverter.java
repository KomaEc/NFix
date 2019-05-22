package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ReferencingMarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectInputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectOutputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.SerializationMembers;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputValidation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ExternalizableConverter implements Converter {
   private Mapper mapper;
   private final ClassLoaderReference classLoaderReference;
   private transient SerializationMembers serializationMembers;

   public ExternalizableConverter(Mapper mapper, ClassLoaderReference classLoaderReference) {
      this.mapper = mapper;
      this.classLoaderReference = classLoaderReference;
      this.serializationMembers = new SerializationMembers();
   }

   /** @deprecated */
   public ExternalizableConverter(Mapper mapper, ClassLoader classLoader) {
      this(mapper, new ClassLoaderReference(classLoader));
   }

   /** @deprecated */
   public ExternalizableConverter(Mapper mapper) {
      this(mapper, ExternalizableConverter.class.getClassLoader());
   }

   public boolean canConvert(Class type) {
      return JVM.canCreateDerivedObjectOutputStream() && Externalizable.class.isAssignableFrom(type);
   }

   public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
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
         try {
            Externalizable externalizable = (Externalizable)source;
            CustomObjectOutputStream.StreamCallback callback = new CustomObjectOutputStream.StreamCallback() {
               public void writeToStream(Object object) {
                  if (object == null) {
                     writer.startNode("null");
                     writer.endNode();
                  } else {
                     ExtendedHierarchicalStreamWriterHelper.startNode(writer, ExternalizableConverter.this.mapper.serializedClass(object.getClass()), object.getClass());
                     context.convertAnother(object);
                     writer.endNode();
                  }

               }

               public void writeFieldsToStream(Map fields) {
                  throw new UnsupportedOperationException();
               }

               public void defaultWriteObject() {
                  throw new UnsupportedOperationException();
               }

               public void flush() {
                  writer.flush();
               }

               public void close() {
                  throw new UnsupportedOperationException("Objects are not allowed to call ObjectOutput.close() from writeExternal()");
               }
            };
            CustomObjectOutputStream objectOutput = CustomObjectOutputStream.getInstance(context, callback);
            externalizable.writeExternal(objectOutput);
            objectOutput.popCallback();
         } catch (IOException var8) {
            throw new ConversionException("Cannot serialize " + source.getClass().getName() + " using Externalization", var8);
         }
      }

   }

   public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
      Class type = context.getRequiredType();

      try {
         Constructor defaultConstructor = type.getDeclaredConstructor((Class[])null);
         if (!defaultConstructor.isAccessible()) {
            defaultConstructor.setAccessible(true);
         }

         final Externalizable externalizable = (Externalizable)defaultConstructor.newInstance((Object[])null);
         CustomObjectInputStream.StreamCallback callback = new CustomObjectInputStream.StreamCallback() {
            public Object readFromStream() {
               reader.moveDown();
               Class type = HierarchicalStreams.readClassType(reader, ExternalizableConverter.this.mapper);
               Object streamItem = context.convertAnother(externalizable, type);
               reader.moveUp();
               return streamItem;
            }

            public Map readFieldsFromStream() {
               throw new UnsupportedOperationException();
            }

            public void defaultReadObject() {
               throw new UnsupportedOperationException();
            }

            public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException {
               throw new NotActiveException("stream inactive");
            }

            public void close() {
               throw new UnsupportedOperationException("Objects are not allowed to call ObjectInput.close() from readExternal()");
            }
         };
         CustomObjectInputStream objectInput = CustomObjectInputStream.getInstance(context, callback, (ClassLoaderReference)this.classLoaderReference);
         externalizable.readExternal(objectInput);
         objectInput.popCallback();
         return this.serializationMembers.callReadResolve(externalizable);
      } catch (NoSuchMethodException var8) {
         throw new ConversionException("Cannot construct " + type.getClass() + ", missing default constructor", var8);
      } catch (InvocationTargetException var9) {
         throw new ConversionException("Cannot construct " + type.getClass(), var9);
      } catch (InstantiationException var10) {
         throw new ConversionException("Cannot construct " + type.getClass(), var10);
      } catch (IllegalAccessException var11) {
         throw new ConversionException("Cannot construct " + type.getClass(), var11);
      } catch (IOException var12) {
         throw new ConversionException("Cannot externalize " + type.getClass(), var12);
      } catch (ClassNotFoundException var13) {
         throw new ConversionException("Cannot externalize " + type.getClass(), var13);
      }
   }

   private Object readResolve() {
      this.serializationMembers = new SerializationMembers();
      return this;
   }
}
