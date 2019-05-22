package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.groovydoc.GroovyAnnotationRef;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyConstructorDoc;
import org.codehaus.groovy.groovydoc.GroovyFieldDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyType;

public class ExternalGroovyClassDoc implements GroovyClassDoc {
   private Class externalClass;
   private final List<GroovyAnnotationRef> annotationRefs;

   public ExternalGroovyClassDoc(Class externalClass) {
      this.externalClass = externalClass;
      this.annotationRefs = new ArrayList();
   }

   public boolean isPrimitive() {
      return this.externalClass.isPrimitive();
   }

   public GroovyAnnotationRef[] annotations() {
      return (GroovyAnnotationRef[])this.annotationRefs.toArray(new GroovyAnnotationRef[this.annotationRefs.size()]);
   }

   public String qualifiedTypeName() {
      return this.externalClass.getName();
   }

   public GroovyClassDoc superclass() {
      Class aClass = this.externalClass.getSuperclass();
      return aClass != null ? new ExternalGroovyClassDoc(aClass) : new ExternalGroovyClassDoc(Object.class);
   }

   public Class externalClass() {
      return this.externalClass;
   }

   public String getTypeSourceDescription() {
      return this.externalClass.isInterface() ? "interface" : "class";
   }

   public String simpleTypeName() {
      return this.qualifiedTypeName();
   }

   public String typeName() {
      return this.qualifiedTypeName();
   }

   public int hashCode() {
      return this.qualifiedTypeName().hashCode();
   }

   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (other == null) {
         return false;
      } else {
         return !(other instanceof ExternalGroovyClassDoc) ? false : this.qualifiedTypeName().equals(((ExternalGroovyClassDoc)other).qualifiedTypeName());
      }
   }

   public GroovyType superclassType() {
      return null;
   }

   public GroovyConstructorDoc[] constructors() {
      return new GroovyConstructorDoc[0];
   }

   public GroovyConstructorDoc[] constructors(boolean filter) {
      return new GroovyConstructorDoc[0];
   }

   public boolean definesSerializableFields() {
      return false;
   }

   public GroovyFieldDoc[] enumConstants() {
      return new GroovyFieldDoc[0];
   }

   public GroovyFieldDoc[] fields() {
      return new GroovyFieldDoc[0];
   }

   public GroovyFieldDoc[] properties() {
      return new GroovyFieldDoc[0];
   }

   public GroovyFieldDoc[] fields(boolean filter) {
      return new GroovyFieldDoc[0];
   }

   public GroovyClassDoc findClass(String className) {
      return null;
   }

   public GroovyClassDoc[] importedClasses() {
      return new GroovyClassDoc[0];
   }

   public GroovyPackageDoc[] importedPackages() {
      return new GroovyPackageDoc[0];
   }

   public GroovyClassDoc[] innerClasses() {
      return new GroovyClassDoc[0];
   }

   public GroovyClassDoc[] innerClasses(boolean filter) {
      return new GroovyClassDoc[0];
   }

   public GroovyClassDoc[] interfaces() {
      return new GroovyClassDoc[0];
   }

   public GroovyType[] interfaceTypes() {
      return new GroovyType[0];
   }

   public boolean isAbstract() {
      return false;
   }

   public boolean isExternalizable() {
      return false;
   }

   public boolean isSerializable() {
      return false;
   }

   public GroovyMethodDoc[] methods() {
      return new GroovyMethodDoc[0];
   }

   public GroovyMethodDoc[] methods(boolean filter) {
      return new GroovyMethodDoc[0];
   }

   public GroovyFieldDoc[] serializableFields() {
      return new GroovyFieldDoc[0];
   }

   public GroovyMethodDoc[] serializationMethods() {
      return new GroovyMethodDoc[0];
   }

   public boolean subclassOf(GroovyClassDoc gcd) {
      return false;
   }

   public String getFullPathName() {
      return null;
   }

   public String getRelativeRootPath() {
      return null;
   }

   public GroovyClassDoc containingClass() {
      return null;
   }

   public GroovyPackageDoc containingPackage() {
      return null;
   }

   public boolean isFinal() {
      return false;
   }

   public boolean isPackagePrivate() {
      return false;
   }

   public boolean isPrivate() {
      return false;
   }

   public boolean isProtected() {
      return false;
   }

   public boolean isPublic() {
      return false;
   }

   public boolean isStatic() {
      return false;
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

   public String commentText() {
      return null;
   }

   public String getRawCommentText() {
      return null;
   }

   public boolean isAnnotationType() {
      return false;
   }

   public boolean isAnnotationTypeElement() {
      return false;
   }

   public boolean isClass() {
      return false;
   }

   public boolean isConstructor() {
      return false;
   }

   public boolean isDeprecated() {
      return false;
   }

   public boolean isEnum() {
      return false;
   }

   public boolean isEnumConstant() {
      return false;
   }

   public boolean isError() {
      return false;
   }

   public boolean isException() {
      return false;
   }

   public boolean isField() {
      return false;
   }

   public boolean isIncluded() {
      return false;
   }

   public boolean isInterface() {
      return false;
   }

   public boolean isMethod() {
      return false;
   }

   public boolean isOrdinaryClass() {
      return false;
   }

   public String name() {
      return this.externalClass.getSimpleName();
   }

   public void setRawCommentText(String arg0) {
   }

   public String firstSentenceCommentText() {
      return null;
   }

   public int compareTo(Object o) {
      return 0;
   }
}
