package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyExecutableMemberDoc;
import org.codehaus.groovy.groovydoc.GroovyParameter;
import org.codehaus.groovy.groovydoc.GroovyType;

public class SimpleGroovyExecutableMemberDoc extends SimpleGroovyMemberDoc implements GroovyExecutableMemberDoc {
   List parameters = new ArrayList();

   public SimpleGroovyExecutableMemberDoc(String name, GroovyClassDoc belongsToClass) {
      super(name, belongsToClass);
   }

   public GroovyParameter[] parameters() {
      return (GroovyParameter[])((GroovyParameter[])this.parameters.toArray(new GroovyParameter[this.parameters.size()]));
   }

   public void add(GroovyParameter parameter) {
      this.parameters.add(parameter);
   }

   public String flatSignature() {
      return null;
   }

   public boolean isNative() {
      return false;
   }

   public boolean isSynchronized() {
      return false;
   }

   public boolean isVarArgs() {
      return false;
   }

   public String signature() {
      return null;
   }

   public GroovyClassDoc[] thrownExceptions() {
      return null;
   }

   public GroovyType[] thrownExceptionTypes() {
      return null;
   }
}
