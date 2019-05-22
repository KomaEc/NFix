package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.AnnotationMemberDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedAnnotationMemberDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class AnnotationMemberDeclaration extends BodyDeclaration<AnnotationMemberDeclaration> implements NodeWithJavadoc<AnnotationMemberDeclaration>, NodeWithSimpleName<AnnotationMemberDeclaration>, NodeWithType<AnnotationMemberDeclaration, Type>, NodeWithPublicModifier<AnnotationMemberDeclaration>, NodeWithAbstractModifier<AnnotationMemberDeclaration>, Resolvable<ResolvedAnnotationMemberDeclaration> {
   private EnumSet<Modifier> modifiers;
   private Type type;
   private SimpleName name;
   @OptionalProperty
   private Expression defaultValue;

   public AnnotationMemberDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new ClassOrInterfaceType(), new SimpleName(), (Expression)null);
   }

   public AnnotationMemberDeclaration(EnumSet<Modifier> modifiers, Type type, String name, Expression defaultValue) {
      this((TokenRange)null, modifiers, new NodeList(), type, new SimpleName(name), defaultValue);
   }

   @AllFieldsConstructor
   public AnnotationMemberDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, Type type, SimpleName name, Expression defaultValue) {
      this((TokenRange)null, modifiers, annotations, type, name, defaultValue);
   }

   public AnnotationMemberDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, Type type, SimpleName name, Expression defaultValue) {
      super(tokenRange, annotations);
      this.setModifiers(modifiers);
      this.setType(type);
      this.setName(name);
      this.setDefaultValue(defaultValue);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getDefaultValue() {
      return Optional.ofNullable(this.defaultValue);
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public SimpleName getName() {
      return this.name;
   }

   public Type getType() {
      return this.type;
   }

   public AnnotationMemberDeclaration removeDefaultValue() {
      return this.setDefaultValue((Expression)null);
   }

   public AnnotationMemberDeclaration setDefaultValue(final Expression defaultValue) {
      if (defaultValue == this.defaultValue) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.DEFAULT_VALUE, this.defaultValue, defaultValue);
         if (this.defaultValue != null) {
            this.defaultValue.setParentNode((Node)null);
         }

         this.defaultValue = defaultValue;
         this.setAsParentNodeOf(defaultValue);
         return this;
      }
   }

   public AnnotationMemberDeclaration setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public AnnotationMemberDeclaration setName(final SimpleName name) {
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

   public AnnotationMemberDeclaration setType(final Type type) {
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

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.defaultValue != null && node == this.defaultValue) {
         this.removeDefaultValue();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public AnnotationMemberDeclaration clone() {
      return (AnnotationMemberDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public AnnotationMemberDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.annotationMemberDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.defaultValue != null && node == this.defaultValue) {
         this.setDefaultValue((Expression)replacementNode);
         return true;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else if (node == this.type) {
         this.setType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isAnnotationMemberDeclaration() {
      return true;
   }

   public AnnotationMemberDeclaration asAnnotationMemberDeclaration() {
      return this;
   }

   public void ifAnnotationMemberDeclaration(Consumer<AnnotationMemberDeclaration> action) {
      action.accept(this);
   }

   public ResolvedAnnotationMemberDeclaration resolve() {
      return (ResolvedAnnotationMemberDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedAnnotationMemberDeclaration.class);
   }

   public Optional<AnnotationMemberDeclaration> toAnnotationMemberDeclaration() {
      return Optional.of(this);
   }
}
