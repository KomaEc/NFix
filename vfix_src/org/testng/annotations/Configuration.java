package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @deprecated */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Configuration {
   boolean beforeTestClass() default false;

   boolean afterTestClass() default false;

   boolean beforeTestMethod() default false;

   boolean afterTestMethod() default false;

   boolean beforeSuite() default false;

   boolean afterSuite() default false;

   boolean beforeTest() default false;

   boolean afterTest() default false;

   String[] beforeGroups() default {};

   String[] afterGroups() default {};

   /** @deprecated */
   @Deprecated
   String[] parameters() default {};

   boolean enabled() default true;

   String[] groups() default {};

   String[] dependsOnGroups() default {};

   String[] dependsOnMethods() default {};

   boolean alwaysRun() default false;

   boolean inheritGroups() default true;

   String description() default "";
}
