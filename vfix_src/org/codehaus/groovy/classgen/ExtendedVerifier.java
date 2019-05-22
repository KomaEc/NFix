package org.codehaus.groovy.classgen;

import java.lang.annotation.Target;
import java.util.Iterator;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

public class ExtendedVerifier implements GroovyClassVisitor {
   public static final String JVM_ERROR_MESSAGE = "Please make sure you are running on a JVM >= 1.5";
   private SourceUnit source;
   private ClassNode currentClass;

   public ExtendedVerifier(SourceUnit sourceUnit) {
      this.source = sourceUnit;
   }

   public void visitClass(ClassNode node) {
      this.currentClass = node;
      if (node.isAnnotationDefinition()) {
         this.visitAnnotations(node, 64);
      } else {
         this.visitAnnotations(node, 1);
      }

      PackageNode packageNode = node.getPackage();
      if (packageNode != null) {
         this.visitAnnotations(packageNode, 128);
      }

      node.visitContents(this);
   }

   public void visitField(FieldNode node) {
      this.visitAnnotations(node, 8);
   }

   public void visitConstructor(ConstructorNode node) {
      this.visitConstructorOrMethod(node, 2);
   }

   public void visitMethod(MethodNode node) {
      this.visitConstructorOrMethod(node, 4);
   }

   private void visitConstructorOrMethod(MethodNode node, int methodTarget) {
      this.visitAnnotations(node, methodTarget);

      for(int i = 0; i < node.getParameters().length; ++i) {
         Parameter parameter = node.getParameters()[i];
         this.visitAnnotations(parameter, 16);
      }

      if (this.currentClass.isAnnotationDefinition() && !node.isStaticConstructor()) {
         ErrorCollector errorCollector = new ErrorCollector(this.source.getConfiguration());
         AnnotationVisitor visitor = new AnnotationVisitor(this.source, errorCollector);
         visitor.setReportClass(this.currentClass);
         visitor.checkReturnType(node.getReturnType(), node);
         if (node.getParameters().length > 0) {
            this.addError("Annotation members may not have parameters.", node.getParameters()[0]);
         }

         if (node.getExceptions().length > 0) {
            this.addError("Annotation members may not have a throws clause.", node.getExceptions()[0]);
         }

         ReturnStatement code = (ReturnStatement)node.getCode();
         if (code != null) {
            visitor.visitExpression(node.getName(), code.getExpression(), node.getReturnType());
            visitor.checkCircularReference(this.currentClass, node.getReturnType(), code.getExpression());
         }

         this.source.getErrorCollector().addCollectorContents(errorCollector);
      }

   }

   public void visitProperty(PropertyNode node) {
   }

   protected void visitAnnotations(AnnotatedNode node, int target) {
      if (!node.getAnnotations().isEmpty()) {
         this.currentClass.setAnnotated(true);
         if (!this.isAnnotationCompatible()) {
            this.addError("Annotations are not supported in the current runtime. Please make sure you are running on a JVM >= 1.5", node);
         } else {
            AnnotationNode visited;
            for(Iterator i$ = node.getAnnotations().iterator(); i$.hasNext(); this.visitDeprecation(node, visited)) {
               AnnotationNode unvisited = (AnnotationNode)i$.next();
               visited = this.visitAnnotation(unvisited);
               boolean isTargetAnnotation = visited.getClassNode().isResolved() && visited.getClassNode().getTypeClass() == Target.class;
               if (!isTargetAnnotation && !visited.isTargetAllowed(target)) {
                  this.addError("Annotation @" + visited.getClassNode().getName() + " is not allowed on element " + AnnotationNode.targetToName(target), visited);
               }
            }

         }
      }
   }

   private void visitDeprecation(AnnotatedNode node, AnnotationNode visited) {
      if (visited.getClassNode().isResolved() && visited.getClassNode().getTypeClass().getName().equals(Deprecated.class.getName())) {
         if (node instanceof MethodNode) {
            MethodNode mn = (MethodNode)node;
            mn.setModifiers(mn.getModifiers() | 131072);
         } else if (node instanceof FieldNode) {
            FieldNode fn = (FieldNode)node;
            fn.setModifiers(fn.getModifiers() | 131072);
         } else if (node instanceof ClassNode) {
            ClassNode cn = (ClassNode)node;
            cn.setModifiers(cn.getModifiers() | 131072);
         }
      }

   }

   private AnnotationNode visitAnnotation(AnnotationNode unvisited) {
      ErrorCollector errorCollector = new ErrorCollector(this.source.getConfiguration());
      AnnotationVisitor visitor = new AnnotationVisitor(this.source, errorCollector);
      AnnotationNode visited = visitor.visit(unvisited);
      this.source.getErrorCollector().addCollectorContents(errorCollector);
      return visited;
   }

   protected boolean isAnnotationCompatible() {
      return "1.5".equals(this.source.getConfiguration().getTargetBytecode());
   }

   protected void addError(String msg, ASTNode expr) {
      this.source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', expr.getLineNumber(), expr.getColumnNumber()), this.source));
   }

   public void visitGenericType(GenericsType genericsType) {
   }
}
