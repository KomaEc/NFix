package org.codehaus.groovy.groovydoc;

public interface GroovyType {
   boolean isPrimitive();

   String qualifiedTypeName();

   String simpleTypeName();

   String typeName();

   String toString();
}
