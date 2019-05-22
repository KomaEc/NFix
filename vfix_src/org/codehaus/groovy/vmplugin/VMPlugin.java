package org.codehaus.groovy.vmplugin;

import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;

public interface VMPlugin {
   void setAdditionalClassInformation(ClassNode var1);

   Class[] getPluginDefaultGroovyMethods();

   void configureAnnotation(AnnotationNode var1);

   void configureClassNode(CompileUnit var1, ClassNode var2);
}
