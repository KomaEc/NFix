package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.VarTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Optional;
import java.util.function.Consumer;

public final class VarType extends Type {
   @AllFieldsConstructor
   public VarType() {
      this((TokenRange)null);
   }

   public VarType(TokenRange tokenRange) {
      super(tokenRange);
      this.customInitialization();
   }

   public VarType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (VarType)super.setAnnotations(annotations);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public String asString() {
      return "var";
   }

   public VarType clone() {
      return (VarType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public VarTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.varTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public ResolvedType resolve() {
      return (ResolvedType)this.getSymbolResolver().toResolvedType(this, ResolvedType.class);
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public boolean isVarType() {
      return true;
   }

   public VarType asVarType() {
      return this;
   }

   public Optional<VarType> toVarType() {
      return Optional.of(this);
   }

   public void ifVarType(Consumer<VarType> action) {
      action.accept(this);
   }
}
