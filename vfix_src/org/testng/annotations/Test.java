package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface Test {
   String[] groups() default {};

   boolean enabled() default true;

   /** @deprecated */
   @Deprecated
   String[] parameters() default {};

   String[] dependsOnGroups() default {};

   String[] dependsOnMethods() default {};

   long timeOut() default 0L;

   long invocationTimeOut() default 0L;

   int invocationCount() default 1;

   int threadPoolSize() default 0;

   int successPercentage() default 100;

   String dataProvider() default "";

   Class<?> dataProviderClass() default Object.class;

   boolean alwaysRun() default false;

   String description() default "";

   Class[] expectedExceptions() default {};

   String expectedExceptionsMessageRegExp() default ".*";

   String suiteName() default "";

   String testName() default "";

   /** @deprecated */
   boolean sequential() default false;

   boolean singleThreaded() default false;

   Class retryAnalyzer() default Class.class;

   boolean skipFailedInvocations() default false;

   boolean ignoreMissingDependencies() default false;

   int priority() default 0;
}
