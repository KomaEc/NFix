package com.gzoltar.shaded.org.pitest.reloc.xstream.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface XStreamImplicit {
   String itemFieldName() default "";

   String keyFieldName() default "";
}
