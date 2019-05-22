package org.codehaus.groovy.tools.gse;

import java.util.Iterator;
import java.util.Set;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.control.SourceUnit;

public class DependencyTracker extends ClassCodeVisitorSupport {
   private Set<String> current;
   private SourceUnit source;
   private StringSetMap cache;

   public DependencyTracker(SourceUnit source, StringSetMap cache) {
      this.source = source;
      this.cache = cache;
   }

   private void addToCache(ClassNode node) {
      if (node.isPrimaryClassNode()) {
         this.current.add(node.getName());
      }
   }

   private void addToCache(ClassNode[] nodes) {
      if (nodes != null) {
         ClassNode[] arr$ = nodes;
         int len$ = nodes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ClassNode node = arr$[i$];
            this.addToCache(node);
         }

      }
   }

   public void visitClass(ClassNode node) {
      Set<String> old = this.current;
      this.current = this.cache.get(node.getName());
      this.addToCache(node);
      this.addToCache(node.getSuperClass());
      this.addToCache(node.getInterfaces());
      super.visitClass(node);
      this.current = old;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClassExpression(ClassExpression expression) {
      super.visitClassExpression(expression);
      this.addToCache(expression.getType());
   }

   public void visitField(FieldNode node) {
      super.visitField(node);
      this.addToCache(node.getType());
   }

   public void visitMethod(MethodNode node) {
      Parameter[] arr$ = node.getParameters();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter p = arr$[i$];
         this.addToCache(p.getType());
      }

      this.addToCache(node.getReturnType());
      this.addToCache(node.getExceptions());
      super.visitMethod(node);
   }

   public void visitArrayExpression(ArrayExpression expression) {
      super.visitArrayExpression(expression);
      this.addToCache(expression.getType());
   }

   public void visitCastExpression(CastExpression expression) {
      super.visitCastExpression(expression);
      this.addToCache(expression.getType());
   }

   public void visitVariableExpression(VariableExpression expression) {
      super.visitVariableExpression(expression);
      this.addToCache(expression.getType());
   }

   public void visitCatchStatement(CatchStatement statement) {
      super.visitCatchStatement(statement);
      this.addToCache(statement.getVariable().getType());
   }

   public void visitAnnotations(AnnotatedNode node) {
      super.visitAnnotations(node);
      Iterator i$ = node.getAnnotations().iterator();

      while(i$.hasNext()) {
         AnnotationNode an = (AnnotationNode)i$.next();
         this.addToCache(an.getClassNode());
      }

   }

   public void visitConstructorCallExpression(ConstructorCallExpression call) {
      super.visitConstructorCallExpression(call);
      this.addToCache(call.getType());
   }
}
