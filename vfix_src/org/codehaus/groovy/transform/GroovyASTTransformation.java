package org.codehaus.groovy.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.groovy.control.CompilePhase;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GroovyASTTransformation {
   CompilePhase phase() default CompilePhase.CANONICALIZATION;
}
