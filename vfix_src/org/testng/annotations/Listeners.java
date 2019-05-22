package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.testng.ITestNGListener;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Listeners {
   Class<? extends ITestNGListener>[] value() default {};
}
