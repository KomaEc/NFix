package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ReceiverParameterMetaModel;
import com.github.javaparser.utils.Utils;

public final class ReceiverParameter extends Node implements NodeWithType<ReceiverParameter, Type>, NodeWithAnnotations<ReceiverParameter>, NodeWithName<ReceiverParameter> {
   private Type type;
   private NodeList<AnnotationExpr> annotations;
   private Name name;

   public ReceiverParameter() {
      this((TokenRange)null, new NodeList(), new ClassOrInterfaceType(), new Name());
   }

   public ReceiverParameter(Type type, Name name) {
      this((TokenRange)null, new NodeList(), type, name);
   }

   public ReceiverParameter(Type type, String name) {
      this((TokenRange)null, new NodeList(), type, new Name(name));
   }

   @AllFieldsConstructor
   public ReceiverParameter(NodeList<AnnotationExpr> annotations, Type type, Name name) {
      this((TokenRange)null, annotations, type, name);
   }

   public ReceiverParameter(TokenRange tokenRange, NodeList<AnnotationExpr> annotations, Type type, Name name) {
      super(tokenRange);
      this.setAnnotations(annotations);
      this.setType(type);
      this.setName(name);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Type getType() {
      return this.type;
   }

   public ReceiverParameter setType(final Type type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         if (this.type != null) {
            this.type.setParentNode((Node)null);
         }

         this.type = type;
         this.setAsParentNodeOf(type);
         return this;
      }
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public ReceiverParameter setAnnotations(final NodeList<AnnotationExpr> annotations) {
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

   public ReceiverParameter clone() {
      return (ReceiverParameter)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ReceiverParameterMetaModel getMetaModel() {
      return JavaParserMetaModel.receiverParameterMetaModel;
   }

   public Name getName() {
      return this.name;
   }

   public ReceiverParameter setName(final Name name) {
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
         } else if (node == this.type) {
            this.setType((Type)replacementNode);
            return true;
         } else {
            return super.replace(node, replacementNode);
         }
      }
   }
}
