package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.groovydoc.GroovyAnnotationRef;
import org.codehaus.groovy.groovydoc.GroovyParameter;
import org.codehaus.groovy.groovydoc.GroovyType;

public class SimpleGroovyParameter implements GroovyParameter {
   private String name;
   private String typeName;
   private String defaultValue;
   private GroovyType type;
   private final List<GroovyAnnotationRef> annotationRefs;

   public SimpleGroovyParameter(String name) {
      this.name = name;
      this.annotationRefs = new ArrayList();
   }

   public String defaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public String name() {
      return this.name;
   }

   public String typeName() {
      return this.type == null ? this.typeName : this.type.simpleTypeName();
   }

   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }

   public GroovyAnnotationRef[] annotations() {
      return (GroovyAnnotationRef[])this.annotationRefs.toArray(new GroovyAnnotationRef[this.annotationRefs.size()]);
   }

   public void addAnnotationRef(GroovyAnnotationRef ref) {
      this.annotationRefs.add(ref);
   }

   public GroovyType type() {
      return this.type;
   }

   public void setType(GroovyType type) {
      this.type = type;
   }

   public boolean isTypeAvailable() {
      return this.type != null;
   }
}
