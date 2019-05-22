package com.github.javaparser.ast.expr;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.VariableDeclarationExprMetaModel;
import com.github.javaparser.utils.Utils;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class VariableDeclarationExpr extends Expression implements NodeWithFinalModifier<VariableDeclarationExpr>, NodeWithAnnotations<VariableDeclarationExpr>, NodeWithVariables<VariableDeclarationExpr> {
   private EnumSet<Modifier> modifiers;
   private NodeList<AnnotationExpr> annotations;
   @NonEmptyProperty
   private NodeList<VariableDeclarator> variables;

   public VariableDeclarationExpr() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new NodeList());
   }

   public VariableDeclarationExpr(final Type type, String variableName) {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), NodeList.nodeList((Node[])(new VariableDeclarator(type, variableName))));
   }

   public VariableDeclarationExpr(VariableDeclarator var) {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), NodeList.nodeList((Node[])(var)));
   }

   public VariableDeclarationExpr(final Type type, String variableName, Modifier... modifiers) {
      this((TokenRange)null, (EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), new NodeList(), NodeList.nodeList((Node[])(new VariableDeclarator(type, variableName))));
   }

   public VariableDeclarationExpr(VariableDeclarator var, Modifier... modifiers) {
      this((TokenRange)null, (EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), new NodeList(), NodeList.nodeList((Node[])(var)));
   }

   public VariableDeclarationExpr(final NodeList<VariableDeclarator> variables) {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), variables);
   }

   public VariableDeclarationExpr(final EnumSet<Modifier> modifiers, final NodeList<VariableDeclarator> variables) {
      this((TokenRange)null, modifiers, new NodeList(), variables);
   }

   @AllFieldsConstructor
   public VariableDeclarationExpr(final EnumSet<Modifier> modifiers, final NodeList<AnnotationExpr> annotations, final NodeList<VariableDeclarator> variables) {
      this((TokenRange)null, modifiers, annotations, variables);
   }

   public VariableDeclarationExpr(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<VariableDeclarator> variables) {
      super(tokenRange);
      this.setModifiers(modifiers);
      this.setAnnotations(annotations);
      this.setVariables(variables);
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

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public NodeList<VariableDeclarator> getVariables() {
      return this.variables;
   }

   public VariableDeclarationExpr setAnnotations(final NodeList<AnnotationExpr> annotations) {
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

   public VariableDeclarationExpr setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public VariableDeclarationExpr setVariables(final NodeList<VariableDeclarator> variables) {
      Utils.assertNotNull(variables);
      if (variables == this.variables) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VARIABLES, this.variables, variables);
         if (this.variables != null) {
            this.variables.setParentNode((Node)null);
         }

         this.variables = variables;
         this.setAsParentNodeOf(variables);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.variables.size(); ++i) {
            if (this.variables.get(i) == node) {
               this.variables.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public VariableDeclarationExpr clone() {
      return (VariableDeclarationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public VariableDeclarationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.variableDeclarationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.annotations.size(); ++i) {
            if (this.annotations.get(i) == node) {
               this.annotations.set(i, (Node)((AnnotationExpr)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.variables.size(); ++i) {
            if (this.variables.get(i) == node) {
               this.variables.set(i, (Node)((VariableDeclarator)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isVariableDeclarationExpr() {
      return true;
   }

   public VariableDeclarationExpr asVariableDeclarationExpr() {
      return this;
   }

   public void ifVariableDeclarationExpr(Consumer<VariableDeclarationExpr> action) {
      action.accept(this);
   }

   public Optional<VariableDeclarationExpr> toVariableDeclarationExpr() {
      return Optional.of(this);
   }
}
