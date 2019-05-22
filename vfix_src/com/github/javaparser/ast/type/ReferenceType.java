package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ReferenceTypeMetaModel;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ReferenceType extends Type {
   public ReferenceType() {
      this((TokenRange)null, new NodeList());
   }

   @AllFieldsConstructor
   public ReferenceType(NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, annotations);
   }

   public ReferenceType(TokenRange tokenRange, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.customInitialization();
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public ReferenceType clone() {
      return (ReferenceType)this.accept(new CloneVisitor(), (Object)null);
   }

   public ReferenceTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.referenceTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isReferenceType() {
      return true;
   }

   public ReferenceType asReferenceType() {
      return this;
   }

   public void ifReferenceType(Consumer<ReferenceType> action) {
      action.accept(this);
   }

   public Optional<ReferenceType> toReferenceType() {
      return Optional.of(this);
   }
}
