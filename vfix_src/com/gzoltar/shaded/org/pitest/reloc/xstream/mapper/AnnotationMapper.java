package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.InitializationException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamAlias;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamAliasType;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamAsAttribute;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamConverters;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamImplicit;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamImplicitCollection;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamInclude;
import com.gzoltar.shaded.org.pitest.reloc.xstream.annotations.XStreamOmitField;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterMatcher;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterRegistry;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverterWrapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.DependencyInjectionFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.TypedNull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationMapper extends MapperWrapper implements AnnotationConfiguration {
   private boolean locked;
   private transient Object[] arguments;
   private final ConverterRegistry converterRegistry;
   private transient ClassAliasingMapper classAliasingMapper;
   private transient DefaultImplementationsMapper defaultImplementationsMapper;
   private transient ImplicitCollectionMapper implicitCollectionMapper;
   private transient FieldAliasingMapper fieldAliasingMapper;
   private transient AttributeMapper attributeMapper;
   private transient LocalConversionMapper localConversionMapper;
   private final Map<Class<?>, Map<List<Object>, Converter>> converterCache;
   private final Set<Class<?>> annotatedTypes;

   public AnnotationMapper(Mapper wrapped, ConverterRegistry converterRegistry, ConverterLookup converterLookup, ClassLoaderReference classLoaderReference, ReflectionProvider reflectionProvider) {
      super(wrapped);
      this.converterCache = new HashMap();
      this.annotatedTypes = Collections.synchronizedSet(new HashSet());
      this.converterRegistry = converterRegistry;
      this.annotatedTypes.add(Object.class);
      this.setupMappers();
      this.locked = true;
      ClassLoader classLoader = classLoaderReference.getReference();
      this.arguments = new Object[]{this, classLoaderReference, reflectionProvider, converterLookup, new JVM(), classLoader != null ? classLoader : new TypedNull(ClassLoader.class)};
   }

   /** @deprecated */
   public AnnotationMapper(Mapper wrapped, ConverterRegistry converterRegistry, ConverterLookup converterLookup, ClassLoader classLoader, ReflectionProvider reflectionProvider, JVM jvm) {
      this(wrapped, converterRegistry, converterLookup, new ClassLoaderReference(classLoader), reflectionProvider);
   }

   public String realMember(Class type, String serialized) {
      if (!this.locked) {
         this.processAnnotations(type);
      }

      return super.realMember(type, serialized);
   }

   public String serializedClass(Class type) {
      if (!this.locked) {
         this.processAnnotations(type);
      }

      return super.serializedClass(type);
   }

   public Class defaultImplementationOf(Class type) {
      if (!this.locked) {
         this.processAnnotations(type);
      }

      Class defaultImplementation = super.defaultImplementationOf(type);
      if (!this.locked) {
         this.processAnnotations(defaultImplementation);
      }

      return defaultImplementation;
   }

   public Converter getLocalConverter(Class definedIn, String fieldName) {
      if (!this.locked) {
         this.processAnnotations(definedIn);
      }

      return super.getLocalConverter(definedIn, fieldName);
   }

   public void autodetectAnnotations(boolean mode) {
      this.locked = !mode;
   }

   public void processAnnotations(Class[] initialTypes) {
      if (initialTypes != null && initialTypes.length != 0) {
         this.locked = true;
         Set<Class<?>> types = new AnnotationMapper.UnprocessedTypesSet();
         Class[] var3 = initialTypes;
         int var4 = initialTypes.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class initialType = var3[var5];
            types.add(initialType);
         }

         this.processTypes(types);
      }
   }

   private void processAnnotations(Class initialType) {
      if (initialType != null) {
         Set<Class<?>> types = new AnnotationMapper.UnprocessedTypesSet();
         types.add(initialType);
         this.processTypes(types);
      }
   }

   private void processTypes(Set<Class<?>> types) {
      while(!types.isEmpty()) {
         Iterator<Class<?>> iter = types.iterator();
         Class<?> type = (Class)iter.next();
         iter.remove();
         synchronized(type) {
            if (!this.annotatedTypes.contains(type)) {
               try {
                  if (!type.isPrimitive()) {
                     this.addParametrizedTypes(type, types);
                     this.processConverterAnnotations(type);
                     this.processAliasAnnotation(type, types);
                     this.processAliasTypeAnnotation(type);
                     if (!type.isInterface()) {
                        this.processImplicitCollectionAnnotation(type);
                        Field[] fields = type.getDeclaredFields();

                        for(int i = 0; i < fields.length; ++i) {
                           Field field = fields[i];
                           if (!field.isEnumConstant() && (field.getModifiers() & 136) <= 0) {
                              this.addParametrizedTypes(field.getGenericType(), types);
                              if (!field.isSynthetic()) {
                                 this.processFieldAliasAnnotation(field);
                                 this.processAsAttributeAnnotation(field);
                                 this.processImplicitAnnotation(field);
                                 this.processOmitFieldAnnotation(field);
                                 this.processLocalConverterAnnotation(field);
                              }
                           }
                        }
                     }
                  }
               } finally {
                  this.annotatedTypes.add(type);
               }
            }
         }
      }

   }

   private void addParametrizedTypes(Type type, final Set<Class<?>> types) {
      final Set<Type> processedTypes = new HashSet();
      LinkedHashSet localTypes = new LinkedHashSet<Type>() {
         public boolean add(Type o) {
            if (o instanceof Class) {
               return types.add((Class)o);
            } else {
               return o != null && !processedTypes.contains(o) ? super.add(o) : false;
            }
         }
      };

      while(type != null) {
         processedTypes.add(type);
         int var8;
         int var9;
         Type[] var16;
         Type iface;
         if (!(type instanceof Class)) {
            Type[] actualArguments;
            if (type instanceof TypeVariable) {
               TypeVariable<?> typeVariable = (TypeVariable)type;
               actualArguments = typeVariable.getBounds();
               var16 = actualArguments;
               var8 = actualArguments.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  iface = var16[var9];
                  localTypes.add(iface);
               }
            } else if (type instanceof ParameterizedType) {
               ParameterizedType parametrizedType = (ParameterizedType)type;
               localTypes.add(parametrizedType.getRawType());
               actualArguments = parametrizedType.getActualTypeArguments();
               var16 = actualArguments;
               var8 = actualArguments.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  iface = var16[var9];
                  localTypes.add(iface);
               }
            } else if (type instanceof GenericArrayType) {
               GenericArrayType arrayType = (GenericArrayType)type;
               localTypes.add(arrayType.getGenericComponentType());
            }
         } else {
            Class<?> clazz = (Class)type;
            types.add(clazz);
            if (!clazz.isPrimitive()) {
               TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
               TypeVariable[] var7 = typeParameters;
               var8 = typeParameters.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  TypeVariable<?> typeVariable = var7[var9];
                  localTypes.add(typeVariable);
               }

               localTypes.add(clazz.getGenericSuperclass());
               var16 = clazz.getGenericInterfaces();
               var8 = var16.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  iface = var16[var9];
                  localTypes.add(iface);
               }
            }
         }

         if (!localTypes.isEmpty()) {
            Iterator<Type> iter = localTypes.iterator();
            type = (Type)iter.next();
            iter.remove();
         } else {
            type = null;
         }
      }

   }

   private void processConverterAnnotations(Class<?> type) {
      if (this.converterRegistry != null) {
         XStreamConverters convertersAnnotation = (XStreamConverters)type.getAnnotation(XStreamConverters.class);
         XStreamConverter converterAnnotation = (XStreamConverter)type.getAnnotation(XStreamConverter.class);
         List<XStreamConverter> annotations = convertersAnnotation != null ? new ArrayList(Arrays.asList(convertersAnnotation.value())) : new ArrayList();
         if (converterAnnotation != null) {
            annotations.add(converterAnnotation);
         }

         Iterator var5 = annotations.iterator();

         while(var5.hasNext()) {
            XStreamConverter annotation = (XStreamConverter)var5.next();
            Converter converter = this.cacheConverter(annotation, converterAnnotation != null ? type : null);
            if (converter != null) {
               if (converterAnnotation == null && !converter.canConvert(type)) {
                  throw new InitializationException("Converter " + annotation.value().getName() + " cannot handle annotated class " + type.getName());
               }

               this.converterRegistry.registerConverter(converter, annotation.priority());
            }
         }
      }

   }

   private void processAliasAnnotation(Class<?> type, Set<Class<?>> types) {
      XStreamAlias aliasAnnotation = (XStreamAlias)type.getAnnotation(XStreamAlias.class);
      if (aliasAnnotation != null) {
         if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
         }

         this.classAliasingMapper.addClassAlias(aliasAnnotation.value(), type);
         if (aliasAnnotation.impl() != Void.class) {
            this.defaultImplementationsMapper.addDefaultImplementation(aliasAnnotation.impl(), type);
            if (type.isInterface()) {
               types.add(aliasAnnotation.impl());
            }
         }
      }

   }

   private void processAliasTypeAnnotation(Class<?> type) {
      XStreamAliasType aliasAnnotation = (XStreamAliasType)type.getAnnotation(XStreamAliasType.class);
      if (aliasAnnotation != null) {
         if (this.classAliasingMapper == null) {
            throw new InitializationException("No " + ClassAliasingMapper.class.getName() + " available");
         }

         this.classAliasingMapper.addTypeAlias(aliasAnnotation.value(), type);
      }

   }

   /** @deprecated */
   @Deprecated
   private void processImplicitCollectionAnnotation(Class<?> type) {
      XStreamImplicitCollection implicitColAnnotation = (XStreamImplicitCollection)type.getAnnotation(XStreamImplicitCollection.class);
      if (implicitColAnnotation != null) {
         if (this.implicitCollectionMapper == null) {
            throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
         }

         String fieldName = implicitColAnnotation.value();
         String itemFieldName = implicitColAnnotation.item();

         Field field;
         try {
            field = type.getDeclaredField(fieldName);
         } catch (NoSuchFieldException var9) {
            throw new InitializationException(type.getName() + " does not have a field named '" + fieldName + "' as required by " + XStreamImplicitCollection.class.getName());
         }

         Class itemType = null;
         Type genericType = field.getGenericType();
         if (genericType instanceof ParameterizedType) {
            Type typeArgument = ((ParameterizedType)genericType).getActualTypeArguments()[0];
            itemType = this.getClass(typeArgument);
         }

         if (itemType == null) {
            this.implicitCollectionMapper.add(type, fieldName, (String)null, Object.class);
         } else if (itemFieldName.equals("")) {
            this.implicitCollectionMapper.add(type, fieldName, (String)null, itemType);
         } else {
            this.implicitCollectionMapper.add(type, fieldName, itemFieldName, itemType);
         }
      }

   }

   private void processFieldAliasAnnotation(Field field) {
      XStreamAlias aliasAnnotation = (XStreamAlias)field.getAnnotation(XStreamAlias.class);
      if (aliasAnnotation != null) {
         if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
         }

         this.fieldAliasingMapper.addFieldAlias(aliasAnnotation.value(), field.getDeclaringClass(), field.getName());
      }

   }

   private void processAsAttributeAnnotation(Field field) {
      XStreamAsAttribute asAttributeAnnotation = (XStreamAsAttribute)field.getAnnotation(XStreamAsAttribute.class);
      if (asAttributeAnnotation != null) {
         if (this.attributeMapper == null) {
            throw new InitializationException("No " + AttributeMapper.class.getName() + " available");
         }

         this.attributeMapper.addAttributeFor(field);
      }

   }

   private void processImplicitAnnotation(Field field) {
      XStreamImplicit implicitAnnotation = (XStreamImplicit)field.getAnnotation(XStreamImplicit.class);
      if (implicitAnnotation != null) {
         if (this.implicitCollectionMapper == null) {
            throw new InitializationException("No " + ImplicitCollectionMapper.class.getName() + " available");
         }

         String fieldName = field.getName();
         String itemFieldName = implicitAnnotation.itemFieldName();
         String keyFieldName = implicitAnnotation.keyFieldName();
         boolean isMap = Map.class.isAssignableFrom(field.getType());
         Class itemType = null;
         if (!field.getType().isArray()) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
               Type[] actualTypeArguments = ((ParameterizedType)genericType).getActualTypeArguments();
               Type typeArgument = actualTypeArguments[isMap ? 1 : 0];
               itemType = this.getClass(typeArgument);
            }
         }

         if (isMap) {
            this.implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemFieldName != null && !"".equals(itemFieldName) ? itemFieldName : null, itemType, keyFieldName != null && !"".equals(keyFieldName) ? keyFieldName : null);
         } else if (itemFieldName != null && !"".equals(itemFieldName)) {
            this.implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemFieldName, itemType);
         } else {
            this.implicitCollectionMapper.add(field.getDeclaringClass(), fieldName, itemType);
         }
      }

   }

   private void processOmitFieldAnnotation(Field field) {
      XStreamOmitField omitFieldAnnotation = (XStreamOmitField)field.getAnnotation(XStreamOmitField.class);
      if (omitFieldAnnotation != null) {
         if (this.fieldAliasingMapper == null) {
            throw new InitializationException("No " + FieldAliasingMapper.class.getName() + " available");
         }

         this.fieldAliasingMapper.omitField(field.getDeclaringClass(), field.getName());
      }

   }

   private void processLocalConverterAnnotation(Field field) {
      XStreamConverter annotation = (XStreamConverter)field.getAnnotation(XStreamConverter.class);
      if (annotation != null) {
         Converter converter = this.cacheConverter(annotation, field.getType());
         if (converter != null) {
            if (this.localConversionMapper == null) {
               throw new InitializationException("No " + LocalConversionMapper.class.getName() + " available");
            }

            this.localConversionMapper.registerLocalConverter(field.getDeclaringClass(), field.getName(), converter);
         }
      }

   }

   private Converter cacheConverter(XStreamConverter annotation, Class targetType) {
      Converter result = null;
      List<Object> parameter = new ArrayList();
      if (targetType != null && annotation.useImplicitType()) {
         parameter.add(targetType);
      }

      List<Object> arrays = new ArrayList();
      arrays.add(annotation.booleans());
      arrays.add(annotation.bytes());
      arrays.add(annotation.chars());
      arrays.add(annotation.doubles());
      arrays.add(annotation.floats());
      arrays.add(annotation.ints());
      arrays.add(annotation.longs());
      arrays.add(annotation.shorts());
      arrays.add(annotation.strings());
      arrays.add(annotation.types());
      Iterator var6 = arrays.iterator();

      while(true) {
         Object converterMapping;
         int size;
         do {
            if (!var6.hasNext()) {
               Class<? extends ConverterMatcher> converterType = annotation.value();
               converterMapping = (Map)this.converterCache.get(converterType);
               if (converterMapping != null) {
                  result = (Converter)((Map)converterMapping).get(parameter);
               }

               if (result == null) {
                  size = parameter.size();
                  Object[] args;
                  if (size > 0) {
                     args = new Object[this.arguments.length + size];
                     System.arraycopy(this.arguments, 0, args, size, this.arguments.length);
                     System.arraycopy(parameter.toArray(new Object[size]), 0, args, 0, size);
                  } else {
                     args = this.arguments;
                  }

                  Object converter;
                  try {
                     if (SingleValueConverter.class.isAssignableFrom(converterType) && !Converter.class.isAssignableFrom(converterType)) {
                        SingleValueConverter svc = (SingleValueConverter)DependencyInjectionFactory.newInstance(converterType, args);
                        converter = new SingleValueConverterWrapper(svc);
                     } else {
                        converter = (Converter)DependencyInjectionFactory.newInstance(converterType, args);
                     }
                  } catch (Exception var12) {
                     throw new InitializationException("Cannot instantiate converter " + converterType.getName() + (targetType != null ? " for type " + targetType.getName() : ""), var12);
                  }

                  if (converterMapping == null) {
                     converterMapping = new HashMap();
                     this.converterCache.put(converterType, converterMapping);
                  }

                  ((Map)converterMapping).put(parameter, converter);
                  result = converter;
               }

               return (Converter)result;
            }

            converterMapping = var6.next();
         } while(converterMapping == null);

         size = Array.getLength(converterMapping);

         for(int i = 0; i < size; ++i) {
            Object object = Array.get(converterMapping, i);
            if (!parameter.contains(object)) {
               parameter.add(object);
            }
         }
      }
   }

   private Class<?> getClass(Type typeArgument) {
      Class<?> type = null;
      if (typeArgument instanceof ParameterizedType) {
         type = (Class)((ParameterizedType)typeArgument).getRawType();
      } else if (typeArgument instanceof Class) {
         type = (Class)typeArgument;
      }

      return type;
   }

   private void setupMappers() {
      this.classAliasingMapper = (ClassAliasingMapper)this.lookupMapperOfType(ClassAliasingMapper.class);
      this.defaultImplementationsMapper = (DefaultImplementationsMapper)this.lookupMapperOfType(DefaultImplementationsMapper.class);
      this.implicitCollectionMapper = (ImplicitCollectionMapper)this.lookupMapperOfType(ImplicitCollectionMapper.class);
      this.fieldAliasingMapper = (FieldAliasingMapper)this.lookupMapperOfType(FieldAliasingMapper.class);
      this.attributeMapper = (AttributeMapper)this.lookupMapperOfType(AttributeMapper.class);
      this.localConversionMapper = (LocalConversionMapper)this.lookupMapperOfType(LocalConversionMapper.class);
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      int max = this.arguments.length - 2;
      out.writeInt(max);

      for(int i = 0; i < max; ++i) {
         out.writeObject(this.arguments[i]);
      }

   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.setupMappers();
      int max = in.readInt();
      this.arguments = new Object[max + 2];

      for(int i = 0; i < max; ++i) {
         this.arguments[i] = in.readObject();
         if (this.arguments[i] instanceof ClassLoaderReference) {
            this.arguments[max + 1] = ((ClassLoaderReference)this.arguments[i]).getReference();
         }
      }

      this.arguments[max] = new JVM();
   }

   private final class UnprocessedTypesSet extends LinkedHashSet<Class<?>> {
      private UnprocessedTypesSet() {
      }

      public boolean add(Class<?> type) {
         if (type == null) {
            return false;
         } else {
            while(type.isArray()) {
               type = type.getComponentType();
            }

            String name = type.getName();
            if (!name.startsWith("java.") && !name.startsWith("javax.")) {
               boolean ret = AnnotationMapper.this.annotatedTypes.contains(type) ? false : super.add(type);
               if (ret) {
                  XStreamInclude inc = (XStreamInclude)type.getAnnotation(XStreamInclude.class);
                  if (inc != null) {
                     Class<?>[] incTypes = inc.value();
                     if (incTypes != null) {
                        Class[] var6 = incTypes;
                        int var7 = incTypes.length;

                        for(int var8 = 0; var8 < var7; ++var8) {
                           Class<?> incType = var6[var8];
                           this.add(incType);
                        }
                     }
                  }
               }

               return ret;
            } else {
               return false;
            }
         }
      }

      // $FF: synthetic method
      UnprocessedTypesSet(Object x1) {
         this();
      }
   }
}
