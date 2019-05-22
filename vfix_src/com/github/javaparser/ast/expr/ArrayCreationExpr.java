package com.github.javaparser.ast.expr;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ArrayCreationExprMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ArrayCreationExpr extends Expression {
   @NonEmptyProperty
   private NodeList<ArrayCreationLevel> levels;
   private Type elementType;
   @OptionalProperty
   private ArrayInitializerExpr initializer;

   public ArrayCreationExpr() {
      this((TokenRange)null, new ClassOrInterfaceType(), new NodeList(), new ArrayInitializerExpr());
   }

   @AllFieldsConstructor
   public ArrayCreationExpr(Type elementType, NodeList<ArrayCreationLevel> levels, ArrayInitializerExpr initializer) {
      this((TokenRange)null, elementType, levels, initializer);
   }

   public ArrayCreationExpr(Type elementType) {
      this((TokenRange)null, elementType, new NodeList(), new ArrayInitializerExpr());
   }

   /** @deprecated */
   @Deprecated
   public ArrayCreationExpr(Range range, Type elementType) {
      this((TokenRange)null, elementType, new NodeList(), new ArrayInitializerExpr());
      this.setRange(range);
   }

   public ArrayCreationExpr(TokenRange tokenRange, Type elementType, NodeList<ArrayCreationLevel> levels, ArrayInitializerExpr initializer) {
      super(tokenRange);
      this.setElementType(elementType);
      this.setLevels(levels);
      this.setInitializer(initializer);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<ArrayInitializerExpr> getInitializer() {
      return Optional.ofNullable(this.initializer);
   }

   public Type getElementType() {
      return this.elementType;
   }

   public ArrayCreationExpr setInitializer(final ArrayInitializerExpr initializer) {
      if (initializer == this.initializer) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INITIALIZER, this.initializer, initializer);
         if (this.initializer != null) {
            this.initializer.setParentNode((Node)null);
         }

         this.initializer = initializer;
         this.setAsParentNodeOf(initializer);
         return this;
      }
   }

   public ArrayCreationExpr setElementType(final Type elementType) {
      Utils.assertNotNull(elementType);
      if (elementType == this.elementType) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ELEMENT_TYPE, this.elementType, elementType);
         if (this.elementType != null) {
            this.elementType.setParentNode((Node)null);
         }

         this.elementType = elementType;
         this.setAsParentNodeOf(elementType);
         return this;
      }
   }

   public NodeList<ArrayCreationLevel> getLevels() {
      return this.levels;
   }

   public ArrayCreationExpr setLevels(final NodeList<ArrayCreationLevel> levels) {
      Utils.assertNotNull(levels);
      if (levels == this.levels) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.LEVELS, this.levels, levels);
         if (this.levels != null) {
            this.levels.setParentNode((Node)null);
         }

         this.levels = levels;
         this.setAsParentNodeOf(levels);
         return this;
      }
   }

   public Type createdType() {
      Type result = this.elementType;

      for(int i = 0; i < this.levels.size(); ++i) {
         result = new ArrayType((Type)result, ArrayType.Origin.TYPE, new NodeList());
      }

      return (Type)result;
   }

   public ArrayCreationExpr setElementType(Class<?> typeClass) {
      this.tryAddImportToParentCompilationUnit(typeClass);
      return this.setElementType(JavaParser.parseType(typeClass.getSimpleName()));
   }

   public ArrayCreationExpr setElementType(final String type) {
      return this.setElementType(JavaParser.parseType(type));
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.initializer != null && node == this.initializer) {
         this.removeInitializer();
         return true;
      } else {
         for(int i = 0; i < this.levels.size(); ++i) {
            if (this.levels.get(i) == node) {
               this.levels.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public ArrayCreationExpr removeInitializer() {
      return this.setInitializer((ArrayInitializerExpr)null);
   }

   public ArrayCreationExpr clone() {
      return (ArrayCreationExpr)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ArrayCreationExprMetaModel getMetaModel() {
      return JavaParserMetaModel.arrayCreationExprMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.elementType) {
         this.setElementType((Type)replacementNode);
         return true;
      } else if (this.initializer != null && node == this.initializer) {
         this.setInitializer((ArrayInitializerExpr)replacementNode);
         return true;
      } else {
         for(int i = 0; i < this.levels.size(); ++i) {
            if (this.levels.get(i) == node) {
               this.levels.set(i, (Node)((ArrayCreationLevel)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isArrayCreationExpr() {
      return true;
   }

   public ArrayCreationExpr asArrayCreationExpr() {
      return this;
   }

   public void ifArrayCreationExpr(Consumer<ArrayCreationExpr> action) {
      action.accept(this);
   }

   public Optional<ArrayCreationExpr> toArrayCreationExpr() {
      return Optional.of(this);
   }
}
