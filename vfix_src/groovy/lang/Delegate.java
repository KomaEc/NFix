package groovy.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
@GroovyASTTransformationClass({"org.codehaus.groovy.transform.DelegateASTTransformation"})
public @interface Delegate {
   boolean interfaces() default true;

   boolean deprecated() default false;
}
