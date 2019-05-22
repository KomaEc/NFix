package org.codehaus.groovy.groovydoc;

public interface GroovyMethodDoc extends GroovyExecutableMemberDoc {
   boolean isAbstract();

   GroovyClassDoc overriddenClass();

   GroovyMethodDoc overriddenMethod();

   GroovyType overriddenType();

   boolean overrides(GroovyMethodDoc var1);

   GroovyType returnType();

   void setReturnType(GroovyType var1);
}
