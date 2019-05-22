package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.TypeMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Type extends Node implements Resolvable<ResolvedType> {
   private NodeList<AnnotationExpr> annotations;

   protected Type(TokenRange range) {
      this(range, new NodeList());
   }

   @AllFieldsConstructor
   public Type(NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, annotations);
   }

   public Type(TokenRange tokenRange, NodeList<AnnotationExpr> annotations) {
      super(tokenRange);
      this.setAnnotations(annotations);
      this.customInitialization();
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public AnnotationExpr getAnnotation(int i) {
      return (AnnotationExpr)this.getAnnotations().get(i);
   }

   public Type setAnnotations(final NodeList<AnnotationExpr> annotations) {
      Utils.assertNotNull(annotations);
      if (annotations == this.annotations) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ANNOTATIONS, this.annotations, annotations);
         if (this.annotations != null) {
            this.annotations.setParentNode((Node)null);
         }

         this.annotations = annotations;
         this.setAsParentNodeOf(annotations);
         return this;
      }
   }

   public Type getElementType() {
      return this instanceof ArrayType ? ((ArrayType)this).getComponentType().getElementType() : this;
   }

   public int getArrayLevel() {
      return this instanceof ArrayType ? 1 + ((ArrayType)this).getComponentType().getArrayLevel() : 0;
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public abstract String asString();

   public Type clone() {
      return (Type)this.accept(new CloneVisitor(), (Object)null);
   }

   public TypeMetaModel getMetaModel() {
      return JavaParserMetaModel.typeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.set(i, (Node)((AnnotationExpr)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isArrayType() {
      return false;
   }

   public ArrayType asArrayType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ArrayType", this));
   }

   public boolean isClassOrInterfaceType() {
      return false;
   }

   public ClassOrInterfaceType asClassOrInterfaceType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ClassOrInterfaceType", this));
   }

   public boolean isIntersectionType() {
      return false;
   }

   public IntersectionType asIntersectionType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an IntersectionType", this));
   }

   public boolean isPrimitiveType() {
      return false;
   }

   public PrimitiveType asPrimitiveType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an PrimitiveType", this));
   }

   public boolean isReferenceType() {
      return false;
   }

   public ReferenceType asReferenceType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an ReferenceType", this));
   }

   public boolean isTypeParameter() {
      return false;
   }

   public TypeParameter asTypeParameter() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an TypeParameter", this));
   }

   public boolean isUnionType() {
      return false;
   }

   public UnionType asUnionType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an UnionType", this));
   }

   public boolean isUnknownType() {
      return false;
   }

   public UnknownType asUnknownType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an UnknownType", this));
   }

   public boolean isVoidType() {
      return false;
   }

   public VoidType asVoidType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an VoidType", this));
   }

   public boolean isWildcardType() {
      return false;
   }

   public WildcardType asWildcardType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an WildcardType", this));
   }

   public void ifArrayType(Consumer<ArrayType> action) {
   }

   public void ifClassOrInterfaceType(Consumer<ClassOrInterfaceType> action) {
   }

   public void ifIntersectionType(Consumer<IntersectionType> action) {
   }

   public void ifPrimitiveType(Consumer<PrimitiveType> action) {
   }

   public void ifReferenceType(Consumer<ReferenceType> action) {
   }

   public void ifTypeParameter(Consumer<TypeParameter> action) {
   }

   public void ifUnionType(Consumer<UnionType> action) {
   }

   public void ifUnknownType(Consumer<UnknownType> action) {
   }

   public void ifVoidType(Consumer<VoidType> action) {
   }

   public void ifWildcardType(Consumer<WildcardType> action) {
   }

   public abstract ResolvedType resolve();

   public Optional<ArrayType> toArrayType() {
      return Optional.empty();
   }

   public Optional<ClassOrInterfaceType> toClassOrInterfaceType() {
      return Optional.empty();
   }

   public Optional<IntersectionType> toIntersectionType() {
      return Optional.empty();
   }

   public Optional<PrimitiveType> toPrimitiveType() {
      return Optional.empty();
   }

   public Optional<ReferenceType> toReferenceType() {
      return Optional.empty();
   }

   public Optional<TypeParameter> toTypeParameter() {
      return Optional.empty();
   }

   public Optional<UnionType> toUnionType() {
      return Optional.empty();
   }

   public Optional<UnknownType> toUnknownType() {
      return Optional.empty();
   }

   public Optional<VoidType> toVoidType() {
      return Optional.empty();
   }

   public Optional<WildcardType> toWildcardType() {
      return Optional.empty();
   }

   public boolean isVarType() {
      return false;
   }

   public VarType asVarType() {
      throw new IllegalStateException(CodeGenerationUtils.f("%s is not an VarType", this));
   }

   public Optional<VarType> toVarType() {
      return Optional.empty();
   }

   public void ifVarType(Consumer<VarType> action) {
   }
}
