package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.ParameterMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;

public final class Parameter extends Node implements NodeWithType<Parameter, Type>, NodeWithAnnotations<Parameter>, NodeWithSimpleName<Parameter>, NodeWithFinalModifier<Parameter>, Resolvable<ResolvedParameterDeclaration> {
   private Type type;
   private boolean isVarArgs;
   private NodeList<AnnotationExpr> varArgsAnnotations;
   private EnumSet<Modifier> modifiers;
   private NodeList<AnnotationExpr> annotations;
   private SimpleName name;

   public Parameter() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new ClassOrInterfaceType(), false, new NodeList(), new SimpleName());
   }

   public Parameter(Type type, SimpleName name) {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), type, false, new NodeList(), name);
   }

   public Parameter(Type type, String name) {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), type, false, new NodeList(), new SimpleName(name));
   }

   public Parameter(EnumSet<Modifier> modifiers, Type type, SimpleName name) {
      this((TokenRange)null, modifiers, new NodeList(), type, false, new NodeList(), name);
   }

   @AllFieldsConstructor
   public Parameter(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, Type type, boolean isVarArgs, NodeList<AnnotationExpr> varArgsAnnotations, SimpleName name) {
      this((TokenRange)null, modifiers, annotations, type, isVarArgs, varArgsAnnotations, name);
   }

   public Parameter(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, Type type, boolean isVarArgs, NodeList<AnnotationExpr> varArgsAnnotations, SimpleName name) {
      super(tokenRange);
      this.setModifiers(modifiers);
      this.setAnnotations(annotations);
      this.setType(type);
      this.setVarArgs(isVarArgs);
      this.setVarArgsAnnotations(varArgsAnnotations);
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

   public boolean isVarArgs() {
      return this.isVarArgs;
   }

   public Parameter setType(final Type type) {
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

   public Parameter setVarArgs(final boolean isVarArgs) {
      if (isVarArgs == this.isVarArgs) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VAR_ARGS, this.isVarArgs, isVarArgs);
         this.isVarArgs = isVarArgs;
         return this;
      }
   }

   public NodeList<AnnotationExpr> getAnnotations() {
      return this.annotations;
   }

   public SimpleName getName() {
      return this.name;
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public Parameter setAnnotations(final NodeList<AnnotationExpr> annotations) {
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

   public Parameter setName(final SimpleName name) {
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

   public Parameter setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
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

         for(i = 0; i < this.varArgsAnnotations.size(); ++i) {
            if (this.varArgsAnnotations.get(i) == node) {
               this.varArgsAnnotations.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public NodeList<AnnotationExpr> getVarArgsAnnotations() {
      return this.varArgsAnnotations;
   }

   public Parameter setVarArgsAnnotations(final NodeList<AnnotationExpr> varArgsAnnotations) {
      Utils.assertNotNull(varArgsAnnotations);
      if (varArgsAnnotations == this.varArgsAnnotations) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VAR_ARGS_ANNOTATIONS, this.varArgsAnnotations, varArgsAnnotations);
         if (this.varArgsAnnotations != null) {
            this.varArgsAnnotations.setParentNode((Node)null);
         }

         this.varArgsAnnotations = varArgsAnnotations;
         this.setAsParentNodeOf(varArgsAnnotations);
         return this;
      }
   }

   public Parameter clone() {
      return (Parameter)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ParameterMetaModel getMetaModel() {
      return JavaParserMetaModel.parameterMetaModel;
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

         if (node == this.name) {
            this.setName((SimpleName)replacementNode);
            return true;
         } else if (node == this.type) {
            this.setType((Type)replacementNode);
            return true;
         } else {
            for(i = 0; i < this.varArgsAnnotations.size(); ++i) {
               if (this.varArgsAnnotations.get(i) == node) {
                  this.varArgsAnnotations.set(i, (Node)((AnnotationExpr)replacementNode));
                  return true;
               }
            }

            return super.replace(node, replacementNode);
         }
      }
   }

   public ResolvedParameterDeclaration resolve() {
      return (ResolvedParameterDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedParameterDeclaration.class);
   }
}
