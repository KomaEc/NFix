package org.codehaus.groovy.ast;

public interface GroovyClassVisitor {
   void visitClass(ClassNode var1);

   void visitConstructor(ConstructorNode var1);

   void visitMethod(MethodNode var1);

   void visitField(FieldNode var1);

   void visitProperty(PropertyNode var1);
}
