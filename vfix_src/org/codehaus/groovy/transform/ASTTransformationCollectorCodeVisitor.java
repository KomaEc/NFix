package org.codehaus.groovy.transform;

import groovy.lang.GroovyClassLoader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;

public class ASTTransformationCollectorCodeVisitor extends ClassCodeVisitorSupport {
   private SourceUnit source;
   private ClassNode classNode;
   private GroovyClassLoader transformLoader;

   public ASTTransformationCollectorCodeVisitor(SourceUnit source, GroovyClassLoader transformLoader) {
      this.source = source;
      this.transformLoader = transformLoader;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClass(ClassNode klassNode) {
      ClassNode oldClass = this.classNode;
      this.classNode = klassNode;
      super.visitClass(this.classNode);
      this.classNode = oldClass;
   }

   public void visitAnnotations(AnnotatedNode node) {
      super.visitAnnotations(node);
      Iterator i$ = node.getAnnotations().iterator();

      while(i$.hasNext()) {
         AnnotationNode annotation = (AnnotationNode)i$.next();
         Annotation transformClassAnnotation = getTransformClassAnnotation(annotation.getClassNode());
         if (transformClassAnnotation != null) {
            this.addTransformsToClassNode(annotation, transformClassAnnotation);
         }
      }

   }

   private void addTransformsToClassNode(AnnotationNode annotation, Annotation transformClassAnnotation) {
      String[] transformClassNames = this.getTransformClassNames(transformClassAnnotation);
      Class[] transformClasses = this.getTransformClasses(transformClassAnnotation);
      if (transformClassNames.length == 0 && transformClasses.length == 0) {
         this.source.getErrorCollector().addError(new SimpleMessage("@GroovyASTTransformationClass in " + annotation.getClassNode().getName() + " does not specify any transform class names/classes", this.source));
      }

      if (transformClassNames.length > 0 && transformClasses.length > 0) {
         this.source.getErrorCollector().addError(new SimpleMessage("@GroovyASTTransformationClass in " + annotation.getClassNode().getName() + " should specify transforms only by class names or by classes and not by both", this.source));
      }

      String[] arr$ = transformClassNames;
      int len$ = transformClassNames.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         String transformClass = arr$[i$];

         try {
            Class klass = this.transformLoader.loadClass(transformClass, false, true, false);
            this.verifyClassAndAddTransform(annotation, klass);
         } catch (ClassNotFoundException var10) {
            this.source.getErrorCollector().addErrorAndContinue(new SimpleMessage("Could not find class for Transformation Processor " + transformClass + " declared by " + annotation.getClassNode().getName(), this.source));
         }
      }

      Class[] arr$ = transformClasses;
      len$ = transformClasses.length;

      for(i$ = 0; i$ < len$; ++i$) {
         Class klass = arr$[i$];
         this.verifyClassAndAddTransform(annotation, klass);
      }

   }

   private void verifyClassAndAddTransform(AnnotationNode annotation, Class klass) {
      if (ASTTransformation.class.isAssignableFrom(klass)) {
         this.classNode.addTransform(klass, annotation);
      } else {
         this.source.getErrorCollector().addError(new SimpleMessage("Not an ASTTransformation: " + klass.getName() + " declared by " + annotation.getClassNode().getName(), this.source));
      }

   }

   private static Annotation getTransformClassAnnotation(ClassNode annotatedType) {
      if (!annotatedType.isResolved()) {
         return null;
      } else {
         Annotation[] arr$ = annotatedType.getTypeClass().getAnnotations();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Annotation ann = arr$[i$];
            if (ann.annotationType().getName().equals(GroovyASTTransformationClass.class.getName())) {
               return ann;
            }
         }

         return null;
      }
   }

   private String[] getTransformClassNames(Annotation transformClassAnnotation) {
      try {
         Method valueMethod = transformClassAnnotation.getClass().getMethod("value");
         return (String[])((String[])valueMethod.invoke(transformClassAnnotation));
      } catch (Exception var3) {
         this.source.addException(var3);
         return new String[0];
      }
   }

   private Class[] getTransformClasses(Annotation transformClassAnnotation) {
      try {
         Method classesMethod = transformClassAnnotation.getClass().getMethod("classes");
         return (Class[])((Class[])classesMethod.invoke(transformClassAnnotation));
      } catch (Exception var3) {
         this.source.addException(var3);
         return new Class[0];
      }
   }
}
