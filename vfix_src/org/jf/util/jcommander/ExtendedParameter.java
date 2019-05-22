package org.jf.util.jcommander;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendedParameter {
   String[] argumentNames();
}
