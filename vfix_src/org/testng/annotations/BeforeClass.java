package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BeforeClass {
   boolean enabled() default true;

   String[] groups() default {};

   String[] dependsOnGroups() default {};

   String[] dependsOnMethods() default {};

   boolean alwaysRun() default false;

   boolean inheritGroups() default true;

   String description() default "";

   long timeOut() default 0L;
}
