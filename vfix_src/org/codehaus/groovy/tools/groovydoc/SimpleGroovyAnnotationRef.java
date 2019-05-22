package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyAnnotationRef;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;

public class SimpleGroovyAnnotationRef implements GroovyAnnotationRef {
   private GroovyClassDoc type;
   private final String desc;
   private String name;

   public SimpleGroovyAnnotationRef(String name, String desc) {
      this.desc = desc;
      this.name = name;
   }

   public void setType(GroovyClassDoc type) {
      this.type = type;
   }

   public GroovyClassDoc type() {
      return this.type;
   }

   public String name() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String description() {
      return this.desc;
   }
}
