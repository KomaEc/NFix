package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyConstructorDoc;

public class SimpleGroovyConstructorDoc extends SimpleGroovyExecutableMemberDoc implements GroovyConstructorDoc {
   public SimpleGroovyConstructorDoc(String name, GroovyClassDoc belongsToClass) {
      super(name, belongsToClass);
   }
}
