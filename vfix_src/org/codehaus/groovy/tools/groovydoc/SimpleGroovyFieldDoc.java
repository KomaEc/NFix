package org.codehaus.groovy.tools.groovydoc;

import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyFieldDoc;
import org.codehaus.groovy.groovydoc.GroovyType;

public class SimpleGroovyFieldDoc extends SimpleGroovyMemberDoc implements GroovyFieldDoc {
   private GroovyType type;
   private String constantValueExpression;

   public SimpleGroovyFieldDoc(String name, GroovyClassDoc belongsToClass) {
      super(name, belongsToClass);
   }

   public Object constantValue() {
      return null;
   }

   public void setConstantValueExpression(String constantValueExpression) {
      this.constantValueExpression = constantValueExpression;
   }

   public String constantValueExpression() {
      return this.constantValueExpression;
   }

   public boolean isTransient() {
      return false;
   }

   public boolean isVolatile() {
      return false;
   }

   public GroovyType type() {
      return this.type;
   }

   public void setType(GroovyType type) {
      this.type = type;
   }
}
