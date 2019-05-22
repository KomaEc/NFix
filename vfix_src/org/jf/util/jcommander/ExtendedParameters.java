package org.jf.util.jcommander;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendedParameters {
   boolean includeParametersInUsage() default false;

   String commandName();

   String[] commandAliases() default {};

   String postfixDescription() default "";
}
