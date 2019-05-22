package com.github.javaparser.ast.type;

import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.TypeParameterMetaModel;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class TypeParameter extends ReferenceType implements NodeWithSimpleName<TypeParameter>, NodeWithAnnotations<TypeParameter> {
   private SimpleName name;
   private NodeList<ClassOrInterfaceType> typeBound;

   public TypeParameter() {
      this((TokenRange)null, new SimpleName(), new NodeList(), new NodeList());
   }

   public TypeParameter(final String name) {
      this((TokenRange)null, new SimpleName(name), new NodeList(), new NodeList());
   }

   public TypeParameter(final String name, final NodeList<ClassOrInterfaceType> typeBound) {
      this((TokenRange)null, new SimpleName(name), typeBound, new NodeList());
   }

   /** @deprecated */
   @Deprecated
   public TypeParameter(Range range, final SimpleName name, final NodeList<ClassOrInterfaceType> typeBound) {
      this((TokenRange)null, name, typeBound, new NodeList());
      this.setRange(range);
   }

   @AllFieldsConstructor
   public TypeParameter(SimpleName name, NodeList<ClassOrInterfaceType> typeBound, NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, name, typeBound, annotations);
   }

   public TypeParameter(TokenRange tokenRange, SimpleName name, NodeList<ClassOrInterfaceType> typeBound, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.setName(name);
      this.setTypeBound(typeBound);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public SimpleName getName() {
      return this.name;
   }

   public NodeList<ClassOrInterfaceType> getTypeBound() {
      return this.typeBound;
   }

   public TypeParameter setName(final SimpleName name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public TypeParameter setTypeBound(final NodeList<ClassOrInterfaceType> typeBound) {
      Utils.assertNotNull(typeBound);
      if (typeBound == this.typeBound) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE_BOUND, this.typeBound, typeBound);
         if (this.typeBound != null) {
            this.typeBound.setParentNode((Node)null);
         }

         this.typeBound = typeBound;
         this.setAsParentNodeOf(typeBound);
         return this;
      }
   }

   public TypeParameter setAnnotations(NodeList<AnnotationExpr> annotations) {
      super.setAnnotations(annotations);
      return this;
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.typeBound.size(); ++i) {
            if (this.typeBound.get(i) == node) {
               this.typeBound.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public String asString() {
      StringBuilder str = new StringBuilder(this.getNameAsString());
      this.getTypeBound().ifNonEmpty((l) -> {
         str.append((String)l.stream().map(ClassOrInterfaceType::asString).collect(Collectors.joining("&", " extends ", "")));
      });
      return str.toString();
   }

   public TypeParameter clone() {
      return (TypeParameter)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public TypeParameterMetaModel getMetaModel() {
      return JavaParserMetaModel.typeParameterMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else {
         for(int i = 0; i < this.typeBound.size(); ++i) {
            if (this.typeBound.get(i) == node) {
               this.typeBound.set(i, (Node)((ClassOrInterfaceType)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isTypeParameter() {
      return true;
   }

   public TypeParameter asTypeParameter() {
      return this;
   }

   public void ifTypeParameter(Consumer<TypeParameter> action) {
      action.accept(this);
   }

   public ResolvedTypeVariable resolve() {
      return (ResolvedTypeVariable)this.getSymbolResolver().toResolvedType(this, ResolvedTypeVariable.class);
   }

   public Optional<TypeParameter> toTypeParameter() {
      return Optional.of(this);
   }
}
