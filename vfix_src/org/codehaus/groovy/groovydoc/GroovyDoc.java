package org.codehaus.groovy.groovydoc;

public interface GroovyDoc extends Comparable {
   String commentText();

   String getRawCommentText();

   boolean isAnnotationType();

   boolean isAnnotationTypeElement();

   boolean isClass();

   boolean isConstructor();

   boolean isDeprecated();

   boolean isEnum();

   boolean isEnumConstant();

   boolean isError();

   boolean isException();

   boolean isField();

   boolean isIncluded();

   boolean isInterface();

   boolean isMethod();

   boolean isOrdinaryClass();

   String name();

   void setRawCommentText(String var1);

   String firstSentenceCommentText();
}
