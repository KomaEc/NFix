package org.codehaus.groovy.groovydoc;

public interface GroovyFieldDoc extends GroovyMemberDoc {
   Object constantValue();

   String constantValueExpression();

   boolean isTransient();

   boolean isVolatile();

   GroovyType type();
}
