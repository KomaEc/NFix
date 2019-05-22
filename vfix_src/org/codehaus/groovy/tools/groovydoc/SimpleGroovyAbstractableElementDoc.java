package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyProgramElementDoc;

public class SimpleGroovyAbstractableElementDoc extends SimpleGroovyProgramElementDoc implements GroovyProgramElementDoc {
   private boolean abstractElement;

   public SimpleGroovyAbstractableElementDoc(String name) {
      super(name);
   }

   public void setAbstract(boolean b) {
      this.abstractElement = b;
   }

   public boolean isAbstract() {
      return this.abstractElement;
   }
}
