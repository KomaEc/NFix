package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.Value;

public abstract class ReferenceType extends TypeDecl implements Cloneable {
   protected Map narrowingConversionTo_TypeDecl_values;
   protected boolean unboxed_computed = false;
   protected TypeDecl unboxed_value;
   protected boolean jvmName_computed = false;
   protected String jvmName_value;

   public void flushCache() {
      super.flushCache();
      this.narrowingConversionTo_TypeDecl_values = null;
      this.unboxed_computed = false;
      this.unboxed_value = null;
      this.jvmName_computed = false;
      this.jvmName_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ReferenceType clone() throws CloneNotSupportedException {
      ReferenceType node = (ReferenceType)super.clone();
      node.narrowingConversionTo_TypeDecl_values = null;
      node.unboxed_computed = false;
      node.unboxed_value = null;
      node.jvmName_computed = false;
      node.jvmName_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
      if (this == type) {
         return v;
      } else {
         return type instanceof PrimitiveType ? type.boxed().emitUnboxingOperation(b, this.emitCastTo(b, v, type.boxed(), location), location) : super.emitCastTo(b, v, type, location);
      }
   }

   public ReferenceType() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public ReferenceType(Modifiers p0, String p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   public ReferenceType(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 1);
   }

   public int getNumBodyDecl() {
      return this.getBodyDeclList().getNumChild();
   }

   public int getNumBodyDeclNoTransform() {
      return this.getBodyDeclListNoTransform().getNumChildNoTransform();
   }

   public BodyDecl getBodyDecl(int i) {
      return (BodyDecl)this.getBodyDeclList().getChild(i);
   }

   public void addBodyDecl(BodyDecl node) {
      List<BodyDecl> list = this.parent != null && state != null ? this.getBodyDeclList() : this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void addBodyDeclNoTransform(BodyDecl node) {
      List<BodyDecl> list = this.getBodyDeclListNoTransform();
      list.addChild(node);
   }

   public void setBodyDecl(BodyDecl node, int i) {
      List<BodyDecl> list = this.getBodyDeclList();
      list.setChild(node, i);
   }

   public List<BodyDecl> getBodyDecls() {
      return this.getBodyDeclList();
   }

   public List<BodyDecl> getBodyDeclsNoTransform() {
      return this.getBodyDeclListNoTransform();
   }

   public List<BodyDecl> getBodyDeclList() {
      List<BodyDecl> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public boolean wideningConversionTo(TypeDecl type) {
      ASTNode$State state = this.state();
      return this.instanceOf(type);
   }

   public boolean narrowingConversionTo(TypeDecl type) {
      if (this.narrowingConversionTo_TypeDecl_values == null) {
         this.narrowingConversionTo_TypeDecl_values = new HashMap(4);
      }

      if (this.narrowingConversionTo_TypeDecl_values.containsKey(type)) {
         return (Boolean)this.narrowingConversionTo_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean narrowingConversionTo_TypeDecl_value = this.narrowingConversionTo_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.narrowingConversionTo_TypeDecl_values.put(type, narrowingConversionTo_TypeDecl_value);
         }

         return narrowingConversionTo_TypeDecl_value;
      }
   }

   private boolean narrowingConversionTo_compute(TypeDecl type) {
      if (type.instanceOf(this)) {
         return true;
      } else if (this.isClassDecl() && !this.getModifiers().isFinal() && type.isInterfaceDecl()) {
         return true;
      } else if (this.isInterfaceDecl() && type.isClassDecl() && !type.getModifiers().isFinal()) {
         return true;
      } else if (this.isInterfaceDecl() && type.instanceOf(this)) {
         return true;
      } else if (this.fullName().equals("java.lang.Object") && type.isInterfaceDecl()) {
         return true;
      } else {
         return this.isArrayDecl() && type.isArrayDecl() && this.elementType().instanceOf(type.elementType());
      }
   }

   public boolean isReferenceType() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isSupertypeOfNullType(NullType type) {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isValidAnnotationMethodReturnType() {
      ASTNode$State state = this.state();
      if (this.isString()) {
         return true;
      } else if (this.fullName().equals("java.lang.Class")) {
         return true;
      } else {
         return this.erasure().fullName().equals("java.lang.Class");
      }
   }

   public boolean unboxingConversionTo(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      return this.unboxed() == typeDecl;
   }

   public TypeDecl unboxed() {
      if (this.unboxed_computed) {
         return this.unboxed_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unboxed_value = this.unboxed_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unboxed_computed = true;
         }

         return this.unboxed_value;
      }
   }

   private TypeDecl unboxed_compute() {
      if (this.packageName().equals("java.lang") && this.isTopLevelType()) {
         String n = this.name();
         if (n.equals("Boolean")) {
            return this.typeBoolean();
         }

         if (n.equals("Byte")) {
            return this.typeByte();
         }

         if (n.equals("Character")) {
            return this.typeChar();
         }

         if (n.equals("Short")) {
            return this.typeShort();
         }

         if (n.equals("Integer")) {
            return this.typeInt();
         }

         if (n.equals("Long")) {
            return this.typeLong();
         }

         if (n.equals("Float")) {
            return this.typeFloat();
         }

         if (n.equals("Double")) {
            return this.typeDouble();
         }
      }

      return this.unknownType();
   }

   public TypeDecl unaryNumericPromotion() {
      ASTNode$State state = this.state();
      return (TypeDecl)(this.isNumericType() && !this.isUnknown() ? this.unboxed().unaryNumericPromotion() : this);
   }

   public TypeDecl binaryNumericPromotion(TypeDecl type) {
      ASTNode$State state = this.state();
      return this.unboxed().binaryNumericPromotion(type);
   }

   public boolean isNumericType() {
      ASTNode$State state = this.state();
      return !this.unboxed().isUnknown() && this.unboxed().isNumericType();
   }

   public boolean isIntegralType() {
      ASTNode$State state = this.state();
      return !this.unboxed().isUnknown() && this.unboxed().isIntegralType();
   }

   public boolean isPrimitive() {
      ASTNode$State state = this.state();
      return !this.unboxed().isUnknown() && this.unboxed().isPrimitive();
   }

   public boolean isBoolean() {
      ASTNode$State state = this.state();
      return this.fullName().equals("java.lang.Boolean") && this.unboxed().isBoolean();
   }

   public boolean supertypeNullType(NullType type) {
      ASTNode$State state = this.state();
      return true;
   }

   public TypeDecl stringPromotion() {
      ASTNode$State state = this.state();
      return this.typeObject();
   }

   public String jvmName() {
      if (this.jvmName_computed) {
         return this.jvmName_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.jvmName_value = this.jvmName_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.jvmName_computed = true;
         }

         return this.jvmName_value;
      }
   }

   private String jvmName_compute() {
      if (!this.isNestedType()) {
         return this.fullName();
      } else {
         return !this.isAnonymous() && !this.isLocalClass() ? this.enclosingType().jvmName() + "$" + this.name() : this.enclosingType().jvmName() + "$" + this.uniqueIndex() + this.name();
      }
   }

   public String referenceClassFieldName() {
      ASTNode$State state = this.state();
      return "class$" + this.jvmName().replace('[', '$').replace('.', '$').replace(';', ' ').trim();
   }

   public TypeDecl typeBoolean() {
      ASTNode$State state = this.state();
      TypeDecl typeBoolean_value = this.getParent().Define_TypeDecl_typeBoolean(this, (ASTNode)null);
      return typeBoolean_value;
   }

   public TypeDecl typeByte() {
      ASTNode$State state = this.state();
      TypeDecl typeByte_value = this.getParent().Define_TypeDecl_typeByte(this, (ASTNode)null);
      return typeByte_value;
   }

   public TypeDecl typeChar() {
      ASTNode$State state = this.state();
      TypeDecl typeChar_value = this.getParent().Define_TypeDecl_typeChar(this, (ASTNode)null);
      return typeChar_value;
   }

   public TypeDecl typeShort() {
      ASTNode$State state = this.state();
      TypeDecl typeShort_value = this.getParent().Define_TypeDecl_typeShort(this, (ASTNode)null);
      return typeShort_value;
   }

   public TypeDecl typeInt() {
      ASTNode$State state = this.state();
      TypeDecl typeInt_value = this.getParent().Define_TypeDecl_typeInt(this, (ASTNode)null);
      return typeInt_value;
   }

   public TypeDecl typeLong() {
      ASTNode$State state = this.state();
      TypeDecl typeLong_value = this.getParent().Define_TypeDecl_typeLong(this, (ASTNode)null);
      return typeLong_value;
   }

   public TypeDecl typeFloat() {
      ASTNode$State state = this.state();
      TypeDecl typeFloat_value = this.getParent().Define_TypeDecl_typeFloat(this, (ASTNode)null);
      return typeFloat_value;
   }

   public TypeDecl typeDouble() {
      ASTNode$State state = this.state();
      TypeDecl typeDouble_value = this.getParent().Define_TypeDecl_typeDouble(this, (ASTNode)null);
      return typeDouble_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
