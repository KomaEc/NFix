package com.github.javaparser.ast.expr;

import com.github.javaparser.JavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NameMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;

public final class Name extends Node implements NodeWithIdentifier<Name>, NodeWithAnnotations<Name> {
   @NonEmptyProperty
   private String identifier;
   @OptionalProperty
   private Name qualifier;
   private NodeList<AnnotationExpr> annotations;

   public Name() {
      this((TokenRange)null, (Name)null, "empty", new NodeList());
   }

   public Name(final String identifier) {
      this((TokenRange)null, (Name)null, identifier, new NodeList());
   }

   public Name(Name qualifier, final String identifier) {
      this((TokenRange)null, qualifier, identifier, new NodeList());
   }

   @AllFieldsConstructor
   public Name(Name qualifier, final String identifier, NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, qualifier, identifier, annotations);
   }

   public Name(TokenRange tokenRange, Name qualifier, String identifier, NodeList<AnnotationExpr> annotations) {
      super(tokenRange);
      this.setQualifier(qualifier);
      this.setIdentifier(identifier);
      this.setAnnotations(annotations);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public Name setIdentifier(final String identifier) {
      Utils.assertNonEmpty(identifier);
      if (identifier == this.identifier) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.IDENTIFIER, this.identifier, identifier);
         this.identifier = identifier;
         return this;
      }
   }

   /** @deprecated */
   @Deprecated
   public static Name parse(String qualifiedName) {
      Utils.assertNonEmpty(qualifiedName);
      return JavaParser.parseName(qualifiedName);
   }

   public String asString() {
      return this.qualifier != null ? this.qualifier.asString() + "." + this.identifier : this.identifier;
   }

   public Optional<Name> getQualifier() {
      return Optional.ofNullable(this.qualifier);
   }

   public Name setQualifier(final Name qualifier) {
      if (qualifier == this.qualifier) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.QUALIFIER, this.qualifier, qualifier);
         if (this.qualifier != null) {
            this.qualifier.setParentNode((Node)null);
         }

         this.qualifier = qualifier;
         this.setAsParentNodeOf(qualifier);
         return this;
      }
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

         if (this.qualifier != null && node == this.qualifier) {
            this.removeQualifier();
            return true;
         } else {
            return super.remove(node);
         }
      }
   }

   public Name removeQualifier() {
      return this.setQualifier((Name)null);
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public Name setAnnotations(final NodeList<AnnotationExpr> annotations) {
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

   public Name clone() {
      return (Name)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public NameMetaModel getMetaModel() {
      return JavaParserMetaModel.nameMetaModel;
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

         if (this.qualifier != null && node == this.qualifier) {
            this.setQualifier((Name)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }

   public boolean isTopLevel() {
      return !this.isInternal();
   }

   public boolean isInternal() {
      return this.getParentNode().filter((parent) -> {
         return parent instanceof Name;
      }).isPresent();
   }
}
