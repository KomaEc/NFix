package com.github.javaparser.ast;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.PackageDeclarationMetaModel;
import com.github.javaparser.utils.Utils;

public final class PackageDeclaration extends Node implements NodeWithAnnotations<PackageDeclaration>, NodeWithName<PackageDeclaration> {
   private NodeList<AnnotationExpr> annotations;
   private Name name;

   public PackageDeclaration() {
      this((TokenRange)null, new NodeList(), new Name());
   }

   public PackageDeclaration(Name name) {
      this((TokenRange)null, new NodeList(), name);
   }

   @AllFieldsConstructor
   public PackageDeclaration(NodeList<AnnotationExpr> annotations, Name name) {
      this((TokenRange)null, annotations, name);
   }

   public PackageDeclaration(TokenRange tokenRange, NodeList<AnnotationExpr> annotations, Name name) {
      super(tokenRange);
      this.annotations = new NodeList();
      this.setAnnotations(annotations);
      this.setName(name);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public Name getName() {
      return this.name;
   }

   public PackageDeclaration setAnnotations(final NodeList<AnnotationExpr> annotations) {
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

   public PackageDeclaration setName(final Name name) {
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

   public PackageDeclaration clone() {
      return (PackageDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public PackageDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.packageDeclarationMetaModel;
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

         if (node == this.name) {
            this.setName((Name)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }
}
