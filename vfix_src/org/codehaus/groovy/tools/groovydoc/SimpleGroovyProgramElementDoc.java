package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.groovydoc.GroovyAnnotationRef;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyProgramElementDoc;

public class SimpleGroovyProgramElementDoc extends SimpleGroovyDoc implements GroovyProgramElementDoc {
   private GroovyPackageDoc packageDoc;
   private boolean staticElement;
   private boolean finalElement;
   private boolean publicScope;
   private boolean protectedScope;
   private boolean packagePrivateScope;
   private boolean privateScope;
   private final List<GroovyAnnotationRef> annotationRefs = new ArrayList();

   public SimpleGroovyProgramElementDoc(String name) {
      super(name);
   }

   public GroovyPackageDoc containingPackage() {
      return this.packageDoc;
   }

   public void setContainingPackage(GroovyPackageDoc packageDoc) {
      this.packageDoc = packageDoc;
   }

   public void setStatic(boolean b) {
      this.staticElement = b;
   }

   public boolean isStatic() {
      return this.staticElement;
   }

   public void setFinal(boolean b) {
      this.finalElement = b;
   }

   public boolean isFinal() {
      return this.finalElement;
   }

   public void setPublic(boolean b) {
      this.publicScope = b;
   }

   public boolean isPublic() {
      return this.publicScope;
   }

   public void setProtected(boolean b) {
      this.protectedScope = b;
   }

   public boolean isProtected() {
      return this.protectedScope;
   }

   public void setPackagePrivate(boolean b) {
      this.packagePrivateScope = b;
   }

   public boolean isPackagePrivate() {
      return this.packagePrivateScope;
   }

   public void setPrivate(boolean b) {
      this.privateScope = b;
   }

   public boolean isPrivate() {
      return this.privateScope;
   }

   public GroovyAnnotationRef[] annotations() {
      return (GroovyAnnotationRef[])this.annotationRefs.toArray(new GroovyAnnotationRef[this.annotationRefs.size()]);
   }

   public void addAnnotationRef(GroovyAnnotationRef ref) {
      this.annotationRefs.add(ref);
   }

   public GroovyClassDoc containingClass() {
      return null;
   }

   public String modifiers() {
      return null;
   }

   public int modifierSpecifier() {
      return 0;
   }

   public String qualifiedName() {
      return null;
   }
}
