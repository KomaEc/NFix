package groovy.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@GroovyASTTransformationClass({"org.codehaus.groovy.transform.NewifyASTTransformation"})
public @interface Newify {
   Class[] value();

   boolean auto() default true;
}
