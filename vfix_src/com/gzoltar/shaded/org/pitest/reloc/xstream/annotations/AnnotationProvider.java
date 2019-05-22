package com.gzoltar.shaded.org.pitest.reloc.xstream.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/** @deprecated */
@Deprecated
public class AnnotationProvider {
   /** @deprecated */
   @Deprecated
   public <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
      return field.getAnnotation(annotationClass);
   }
}
