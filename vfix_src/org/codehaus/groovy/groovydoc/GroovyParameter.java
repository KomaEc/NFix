package org.codehaus.groovy.groovydoc;

public interface GroovyParameter {
   GroovyAnnotationRef[] annotations();

   String name();

   GroovyType type();

   String typeName();

   String defaultValue();
}
