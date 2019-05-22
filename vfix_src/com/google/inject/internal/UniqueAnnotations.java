package com.google.inject.internal;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueAnnotations {
   private static final AtomicInteger nextUniqueValue = new AtomicInteger(1);

   private UniqueAnnotations() {
   }

   public static Annotation create() {
      return create(nextUniqueValue.getAndIncrement());
   }

   static Annotation create(final int value) {
      return new UniqueAnnotations.Internal() {
         public int value() {
            return value;
         }

         public Class<? extends Annotation> annotationType() {
            return UniqueAnnotations.Internal.class;
         }

         public String toString() {
            String var1 = String.valueOf(String.valueOf(UniqueAnnotations.Internal.class.getName()));
            int var2 = value;
            return (new StringBuilder(20 + var1.length())).append("@").append(var1).append("(value=").append(var2).append(")").toString();
         }

         public boolean equals(Object o) {
            return o instanceof UniqueAnnotations.Internal && ((UniqueAnnotations.Internal)o).value() == this.value();
         }

         public int hashCode() {
            return 127 * "value".hashCode() ^ value;
         }
      };
   }

   @Retention(RetentionPolicy.RUNTIME)
   @BindingAnnotation
   @interface Internal {
      int value();
   }
}
