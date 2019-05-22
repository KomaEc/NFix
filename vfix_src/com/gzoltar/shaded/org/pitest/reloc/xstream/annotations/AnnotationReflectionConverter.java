package com.gzoltar.shaded.org.pitest.reloc.xstream.annotations;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterMatcher;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverterWrapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/** @deprecated */
@Deprecated
public class AnnotationReflectionConverter extends ReflectionConverter {
   private final AnnotationProvider annotationProvider;
   private final Map<Class<? extends ConverterMatcher>, Converter> cachedConverters;

   /** @deprecated */
   @Deprecated
   public AnnotationReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider, AnnotationProvider annotationProvider) {
      super(mapper, reflectionProvider);
      this.annotationProvider = annotationProvider;
      this.cachedConverters = new HashMap();
   }

   protected void marshallField(MarshallingContext context, Object newObj, Field field) {
      XStreamConverter annotation = (XStreamConverter)this.annotationProvider.getAnnotation(field, XStreamConverter.class);
      if (annotation != null) {
         Class<? extends ConverterMatcher> type = annotation.value();
         this.ensureCache(type);
         context.convertAnother(newObj, (Converter)this.cachedConverters.get(type));
      } else {
         context.convertAnother(newObj);
      }

   }

   private void ensureCache(Class<? extends ConverterMatcher> type) {
      if (!this.cachedConverters.containsKey(type)) {
         this.cachedConverters.put(type, this.newInstance(type));
      }

   }

   protected Object unmarshallField(UnmarshallingContext context, Object result, Class type, Field field) {
      XStreamConverter annotation = (XStreamConverter)this.annotationProvider.getAnnotation(field, XStreamConverter.class);
      if (annotation != null) {
         Class<? extends Converter> converterType = annotation.value();
         this.ensureCache(converterType);
         return context.convertAnother(result, type, (Converter)this.cachedConverters.get(converterType));
      } else {
         return context.convertAnother(result, type);
      }
   }

   private Converter newInstance(Class<? extends ConverterMatcher> type) {
      try {
         Object converter;
         if (SingleValueConverter.class.isAssignableFrom(type)) {
            SingleValueConverter svc = (SingleValueConverter)type.getConstructor().newInstance();
            converter = new SingleValueConverterWrapper(svc);
         } else {
            converter = (Converter)type.getConstructor().newInstance();
         }

         return (Converter)converter;
      } catch (InvocationTargetException var4) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var4.getCause());
      } catch (InstantiationException var5) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var5);
      } catch (IllegalAccessException var6) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var6);
      } catch (NoSuchMethodException var7) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var7);
      }
   }
}
