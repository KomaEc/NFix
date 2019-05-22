package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyType;

public class SimpleGroovyMethodDoc extends SimpleGroovyExecutableMemberDoc implements GroovyMethodDoc {
   private GroovyType returnType;

   public SimpleGroovyMethodDoc(String name, GroovyClassDoc belongsToClass) {
      super(name, belongsToClass);
   }

   public GroovyType returnType() {
      return this.returnType;
   }

   public void setReturnType(GroovyType returnType) {
      this.returnType = returnType;
   }

   public boolean isAbstract() {
      return false;
   }

   public GroovyClassDoc overriddenClass() {
      return null;
   }

   public GroovyMethodDoc overriddenMethod() {
      return null;
   }

   public GroovyType overriddenType() {
      return null;
   }

   public boolean overrides(GroovyMethodDoc arg0) {
      return false;
   }
}
