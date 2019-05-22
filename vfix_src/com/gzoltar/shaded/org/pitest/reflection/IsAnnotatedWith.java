package com.gzoltar.shaded.org.pitest.reflection;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

public class IsAnnotatedWith implements Predicate<AccessibleObject> {
   private final Class<? extends Annotation> clazz;

   public static IsAnnotatedWith instance(Class<? extends Annotation> clazz) {
      return new IsAnnotatedWith(clazz);
   }

   public IsAnnotatedWith(Class<? extends Annotation> clazz) {
      this.clazz = clazz;
   }

   public Boolean apply(AccessibleObject a) {
      return a.isAnnotationPresent(this.clazz);
   }
}
