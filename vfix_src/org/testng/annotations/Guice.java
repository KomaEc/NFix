package org.testng.annotations;

import com.google.inject.Module;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.testng.IModuleFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Guice {
   Class<? extends Module>[] modules() default {};

   Class<? extends IModuleFactory> moduleFactory() default IModuleFactory.class;
}
