package com.github.javaparser.ast.type;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.WildcardTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import java.util.Optional;
import java.util.function.Consumer;

public final class WildcardType extends Type implements NodeWithAnnotations<WildcardType> {
   @OptionalProperty
   private ReferenceType extendedType;
   @OptionalProperty
   private ReferenceType superType;

   public WildcardType() {
      this((TokenRange)null, (ReferenceType)null, (ReferenceType)null, new NodeList());
   }

   public WildcardType(final ReferenceType extendedType) {
      this((TokenRange)null, extendedType, (ReferenceType)null, new NodeList());
   }

   @AllFieldsConstructor
   public WildcardType(final ReferenceType extendedType, final ReferenceType superType, final NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, extendedType, superType, annotations);
   }

   public WildcardType(TokenRange tokenRange, ReferenceType extendedType, ReferenceType superType, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.setExtendedType(extendedType);
      this.setSuperType(superType);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<ReferenceType> getExtendedType() {
      return Optional.ofNullable(this.extendedType);
   }

   public Optional<ReferenceType> getSuperType() {
      return Optional.ofNullable(this.superType);
   }

   /** @deprecated */
   @Deprecated
   public Optional<ReferenceType> getExtendedTypes() {
      return this.getExtendedType();
   }

   /** @deprecated */
   @Deprecated
   public Optional<ReferenceType> getSuperTypes() {
      return this.getSuperType();
   }

   public WildcardType setExtendedType(final ReferenceType extendedType) {
      if (extendedType == this.extendedType) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.EXTENDED_TYPE, this.extendedType, extendedType);
         if (this.extendedType != null) {
            this.extendedType.setParentNode((Node)null);
         }

         this.extendedType = extendedType;
         this.setAsParentNodeOf(extendedType);
         return this;
      }
   }

   /** @deprecated */
   @Deprecated
   public WildcardType setExtendedTypes(final ReferenceType extendedType) {
      return this.setExtendedType(extendedType);
   }

   public WildcardType setSuperType(final ReferenceType superType) {
      if (superType == this.superType) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.SUPER_TYPE, this.superType, superType);
         if (this.superType != null) {
            this.superType.setParentNode((Node)null);
         }

         this.superType = superType;
         this.setAsParentNodeOf(superType);
         return this;
      }
   }

   /** @deprecated */
   @Deprecated
   public WildcardType setSuperTypes(final ReferenceType superType) {
      return this.setSuperType(superType);
   }

   public WildcardType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (WildcardType)super.setAnnotations(annotations);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.extendedType != null && node == this.extendedType) {
         this.removeExtendedType();
         return true;
      } else if (this.superType != null && node == this.superType) {
         this.removeSuperType();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public String asString() {
      StringBuilder str = new StringBuilder("?");
      this.getExtendedType().ifPresent((t) -> {
         str.append(" extends ").append(t.asString());
      });
      this.getSuperType().ifPresent((t) -> {
         str.append(" super ").append(t.asString());
      });
      return str.toString();
   }

   /** @deprecated */
   @Deprecated
   public WildcardType removeExtendedTypes() {
      return this.removeExtendedType();
   }

   /** @deprecated */
   @Deprecated
   public WildcardType removeSuperTypes() {
      return this.removeSuperType();
   }

   public WildcardType removeExtendedType() {
      return this.setExtendedType((ReferenceType)null);
   }

   public WildcardType removeSuperType() {
      return this.setSuperType((ReferenceType)null);
   }

   public WildcardType clone() {
      return (WildcardType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public WildcardTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.wildcardTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.extendedType != null && node == this.extendedType) {
         this.setExtendedType((ReferenceType)replacementNode);
         return true;
      } else if (this.superType != null && node == this.superType) {
         this.setSuperType((ReferenceType)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public WildcardType(TokenRange tokenRange, ReferenceType extendedType, ReferenceType superType) {
      super(tokenRange);
      this.setExtendedType(extendedType);
      this.setSuperType(superType);
      this.customInitialization();
   }

   public boolean isWildcardType() {
      return true;
   }

   public WildcardType asWildcardType() {
      return this;
   }

   public void ifWildcardType(Consumer<WildcardType> action) {
      action.accept(this);
   }

   public ResolvedWildcard resolve() {
      return (ResolvedWildcard)this.getSymbolResolver().toResolvedType(this, ResolvedWildcard.class);
   }

   public Optional<WildcardType> toWildcardType() {
      return Optional.of(this);
   }
}
