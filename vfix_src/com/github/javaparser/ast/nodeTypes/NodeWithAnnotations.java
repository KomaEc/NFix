package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import java.lang.annotation.Annotation;
import java.util.Optional;

public interface NodeWithAnnotations<N extends Node> {
   NodeList<AnnotationExpr> getAnnotations();

   N setAnnotations(NodeList<AnnotationExpr> annotations);

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default AnnotationExpr getAnnotation(int i) {
      return (AnnotationExpr)this.getAnnotations().get(i);
   }

   default N setAnnotation(int i, AnnotationExpr element) {
      this.getAnnotations().set(i, (Node)element);
      return (Node)this;
   }

   default N addAnnotation(AnnotationExpr element) {
      this.getAnnotations().add((Node)element);
      return (Node)this;
   }

   default N addAnnotation(String name) {
      NormalAnnotationExpr annotation = new NormalAnnotationExpr(JavaParser.parseName(name), new NodeList());
      this.addAnnotation((AnnotationExpr)annotation);
      return (Node)this;
   }

   default NormalAnnotationExpr addAndGetAnnotation(String name) {
      NormalAnnotationExpr annotation = new NormalAnnotationExpr(JavaParser.parseName(name), new NodeList());
      this.addAnnotation((AnnotationExpr)annotation);
      return annotation;
   }

   default N addAnnotation(Class<? extends Annotation> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addAnnotation(clazz.getSimpleName());
   }

   default NormalAnnotationExpr addAndGetAnnotation(Class<? extends Annotation> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addAndGetAnnotation(clazz.getSimpleName());
   }

   default N addMarkerAnnotation(String name) {
      MarkerAnnotationExpr markerAnnotationExpr = new MarkerAnnotationExpr(JavaParser.parseName(name));
      this.addAnnotation((AnnotationExpr)markerAnnotationExpr);
      return (Node)this;
   }

   default N addMarkerAnnotation(Class<? extends Annotation> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addMarkerAnnotation(clazz.getSimpleName());
   }

   default N addSingleMemberAnnotation(String name, Expression expression) {
      SingleMemberAnnotationExpr singleMemberAnnotationExpr = new SingleMemberAnnotationExpr(JavaParser.parseName(name), expression);
      return this.addAnnotation((AnnotationExpr)singleMemberAnnotationExpr);
   }

   default N addSingleMemberAnnotation(Class<? extends Annotation> clazz, Expression expression) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addSingleMemberAnnotation(clazz.getSimpleName(), expression);
   }

   default N addSingleMemberAnnotation(String name, String value) {
      return this.addSingleMemberAnnotation(name, JavaParser.parseExpression(value));
   }

   default N addSingleMemberAnnotation(Class<? extends Annotation> clazz, String value) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addSingleMemberAnnotation(clazz.getSimpleName(), value);
   }

   default boolean isAnnotationPresent(String annotationName) {
      return this.getAnnotations().stream().anyMatch((a) -> {
         return a.getName().getIdentifier().equals(annotationName);
      });
   }

   default boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
      return this.isAnnotationPresent(annotationClass.getSimpleName());
   }

   default Optional<AnnotationExpr> getAnnotationByName(String annotationName) {
      return this.getAnnotations().stream().filter((a) -> {
         return a.getName().getIdentifier().equals(annotationName);
      }).findFirst();
   }

   default Optional<AnnotationExpr> getAnnotationByClass(Class<? extends Annotation> annotationClass) {
      return this.getAnnotationByName(annotationClass.getSimpleName());
   }
}
