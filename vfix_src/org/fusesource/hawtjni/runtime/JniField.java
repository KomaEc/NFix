package org.fusesource.hawtjni.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JniField {
   String cast() default "";

   String accessor() default "";

   String conditional() default "";

   FieldFlag[] flags() default {};
}
