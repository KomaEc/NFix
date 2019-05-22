package com.github.javaparser.ast.type;

import com.github.javaparser.JavaParser;
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
import com.github.javaparser.metamodel.PrimitiveTypeMetaModel;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.utils.Utils;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public final class PrimitiveType extends Type implements NodeWithAnnotations<PrimitiveType> {
   static final HashMap<String, PrimitiveType.Primitive> unboxMap = new HashMap();
   private PrimitiveType.Primitive type;

   public static PrimitiveType booleanType() {
      return new PrimitiveType(PrimitiveType.Primitive.BOOLEAN);
   }

   public static PrimitiveType charType() {
      return new PrimitiveType(PrimitiveType.Primitive.CHAR);
   }

   public static PrimitiveType byteType() {
      return new PrimitiveType(PrimitiveType.Primitive.BYTE);
   }

   public static PrimitiveType shortType() {
      return new PrimitiveType(PrimitiveType.Primitive.SHORT);
   }

   public static PrimitiveType intType() {
      return new PrimitiveType(PrimitiveType.Primitive.INT);
   }

   public static PrimitiveType longType() {
      return new PrimitiveType(PrimitiveType.Primitive.LONG);
   }

   public static PrimitiveType floatType() {
      return new PrimitiveType(PrimitiveType.Primitive.FLOAT);
   }

   public static PrimitiveType doubleType() {
      return new PrimitiveType(PrimitiveType.Primitive.DOUBLE);
   }

   public PrimitiveType() {
      this((TokenRange)null, PrimitiveType.Primitive.INT, new NodeList());
   }

   public PrimitiveType(final PrimitiveType.Primitive type) {
      this((TokenRange)null, type, new NodeList());
   }

   @AllFieldsConstructor
   public PrimitiveType(final PrimitiveType.Primitive type, NodeList<AnnotationExpr> annotations) {
      this((TokenRange)null, type, annotations);
   }

   public PrimitiveType(TokenRange tokenRange, PrimitiveType.Primitive type, NodeList<AnnotationExpr> annotations) {
      super(tokenRange, annotations);
      this.setType(type);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public PrimitiveType.Primitive getType() {
      return this.type;
   }

   public ClassOrInterfaceType toBoxedType() {
      return this.type.toBoxedType();
   }

   public PrimitiveType setType(final PrimitiveType.Primitive type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         this.type = type;
         return this;
      }
   }

   public String asString() {
      return this.type.asString();
   }

   public PrimitiveType setAnnotations(NodeList<AnnotationExpr> annotations) {
      return (PrimitiveType)super.setAnnotations(annotations);
   }

   public boolean remove(Node node) {
      return node == null ? false : super.remove(node);
   }

   public PrimitiveType clone() {
      return (PrimitiveType)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public PrimitiveTypeMetaModel getMetaModel() {
      return JavaParserMetaModel.primitiveTypeMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      return node == null ? false : super.replace(node, replacementNode);
   }

   public boolean isPrimitiveType() {
      return true;
   }

   public PrimitiveType asPrimitiveType() {
      return this;
   }

   public void ifPrimitiveType(Consumer<PrimitiveType> action) {
      action.accept(this);
   }

   public ResolvedPrimitiveType resolve() {
      return (ResolvedPrimitiveType)this.getSymbolResolver().toResolvedType(this, ResolvedPrimitiveType.class);
   }

   public Optional<PrimitiveType> toPrimitiveType() {
      return Optional.of(this);
   }

   static {
      PrimitiveType.Primitive[] var0 = PrimitiveType.Primitive.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         PrimitiveType.Primitive unboxedType = var0[var2];
         unboxMap.put(unboxedType.nameOfBoxedType, unboxedType);
      }

   }

   public static enum Primitive {
      BOOLEAN("Boolean"),
      CHAR("Character"),
      BYTE("Byte"),
      SHORT("Short"),
      INT("Integer"),
      LONG("Long"),
      FLOAT("Float"),
      DOUBLE("Double");

      final String nameOfBoxedType;
      private String codeRepresentation;

      public ClassOrInterfaceType toBoxedType() {
         return JavaParser.parseClassOrInterfaceType(this.nameOfBoxedType);
      }

      public String asString() {
         return this.codeRepresentation;
      }

      private Primitive(String nameOfBoxedType) {
         this.nameOfBoxedType = nameOfBoxedType;
         this.codeRepresentation = this.name().toLowerCase();
      }
   }
}
