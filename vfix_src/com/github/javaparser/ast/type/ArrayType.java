package com.github.javaparser.ast.type;

import com.github.javaparser.Range;
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
import com.github.javaparser.metamodel.ArrayTypeMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.utils.Pair;
import com.github.javaparser.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class ArrayType extends ReferenceType implements NodeWithAnnotations<ArrayType> {
   private Type componentType;
   private ArrayType.Origin origin;

   public ResolvedArrayType resolve() {
      return (ResolvedArrayType)this.getSymbolResolver().toResolvedType(this, ResolvedArrayType.class);
   }

   @AllFieldsConstructor
   public ArrayType(Type componentType, ArrayType.Origin origin, NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, componentType, origin, annotations);
   }

   public ArrayType(Type type, AnnotationExpr... annotations) {
      this(type, ArrayType.Origin.TYPE, NodeList.nodeList((Node[])annotations));
   }

   public ArrayType(TokenRange tokenRange, Type componentType, ArrayType.Origin origin, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.setComponentType(componentType);
      this.setOrigin(origin);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Type getComponentType() {
      return this.componentType;
   }

   public ArrayType setComponentType(final Type componentType) {
      Utils.assertNotNull(componentType);
      if (componentType == this.componentType) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.COMPONENT_TYPE, this.componentType, componentType);
         if (this.componentType != null) {
            this.componentType.setParentNode((Node)null);
         }

         this.componentType = componentType;
         this.setAsParentNodeOf(componentType);
         return this;
      }
   }

   @SafeVarargs
   public static Type wrapInArrayTypes(Type type, List<ArrayType.ArrayBracketPair>... arrayBracketPairLists) {
      for(int i = arrayBracketPairLists.length - 1; i >= 0; --i) {
         List<ArrayType.ArrayBracketPair> arrayBracketPairList = arrayBracketPairLists[i];
         if (arrayBracketPairList != null) {
            for(int j = arrayBracketPairList.size() - 1; j >= 0; --j) {
               ArrayType.ArrayBracketPair pair = (ArrayType.ArrayBracketPair)arrayBracketPairList.get(j);
               TokenRange tokenRange = null;
               if (((Type)type).getTokenRange().isPresent() && pair.getTokenRange().isPresent()) {
                  tokenRange = new TokenRange(((TokenRange)((Type)type).getTokenRange().get()).getBegin(), ((TokenRange)pair.getTokenRange().get()).getEnd());
               }

               type = new ArrayType(tokenRange, (Type)type, pair.getOrigin(), pair.getAnnotations());
               if (tokenRange != null) {
                  ((Type)type).setRange((Range)tokenRange.toRange().get());
               }
            }
         }
      }

      return (Type)type;
   }

   public static Pair<Type, List<ArrayType.ArrayBracketPair>> unwrapArrayTypes(Type type) {
      ArrayList arrayBracketPairs;
      ArrayType arrayType;
      for(arrayBracketPairs = new ArrayList(0); type instanceof ArrayType; type = arrayType.getComponentType()) {
         arrayType = (ArrayType)type;
         arrayBracketPairs.add(new ArrayType.ArrayBracketPair((TokenRange)type.getTokenRange().orElse((Object)null), arrayType.getOrigin(), arrayType.getAnnotations()));
      }

      return new Pair(type, arrayBracketPairs);
   }

   public ArrayType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (ArrayType)super.setAnnotations(annotations);
   }

   public ArrayType.Origin getOrigin() {
      return this.origin;
   }

   public ArrayType setOrigin(final ArrayType.Origin origin) {
      Utils.assertNotNull(origin);
      if (origin == this.origin) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.ORIGIN, this.origin, origin);
         this.origin = origin;
         return this;
      }
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public String asString() {
      return this.componentType.asString() + "[]";
   }

   public ArrayType clone() {
      return (ArrayType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ArrayTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.arrayTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.componentType) {
         this.setComponentType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public boolean isArrayType() {
      return true;
   }

   public ArrayType asArrayType() {
      return this;
   }

   public void ifArrayType(Consumer<ArrayType> action) {
      action.accept(this);
   }

   public Optional<ArrayType> toArrayType() {
      return Optional.of(this);
   }

   public static class ArrayBracketPair {
      private TokenRange tokenRange;
      private NodeList<AnnotationExpr> annotations = new NodeList();
      private ArrayType.Origin origin;

      public ArrayBracketPair(TokenRange tokenRange, ArrayType.Origin origin, NodeList<AnnotationExpr> annotations) {
         this.setTokenRange(tokenRange);
         this.setAnnotations(annotations);
         this.setOrigin(origin);
      }

      public NodeList<AnnotationExpr> getAnnotations() {
         return this.annotations;
      }

      public ArrayType.ArrayBracketPair setAnnotations(NodeList<AnnotationExpr> annotations) {
         this.annotations = (NodeList)Utils.assertNotNull(annotations);
         return this;
      }

      public ArrayType.ArrayBracketPair setTokenRange(TokenRange range) {
         this.tokenRange = range;
         return this;
      }

      public Optional<TokenRange> getTokenRange() {
         return Optional.ofNullable(this.tokenRange);
      }

      public ArrayType.Origin getOrigin() {
         return this.origin;
      }

      public ArrayType.ArrayBracketPair setOrigin(ArrayType.Origin origin) {
         this.origin = (ArrayType.Origin)Utils.assertNotNull(origin);
         return this;
      }
   }

   public static enum Origin {
      NAME,
      TYPE;
   }
}
