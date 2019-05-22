package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.Value;

public abstract class NumericType extends PrimitiveType implements Cloneable {
   protected boolean unaryNumericPromotion_computed = false;
   protected TypeDecl unaryNumericPromotion_value;
   protected Map binaryNumericPromotion_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.unaryNumericPromotion_computed = false;
      this.unaryNumericPromotion_value = null;
      this.binaryNumericPromotion_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public NumericType clone() throws CloneNotSupportedException {
      NumericType node = (NumericType)super.clone();
      node.unaryNumericPromotion_computed = false;
      node.unaryNumericPromotion_value = null;
      node.binaryNumericPromotion_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
      if (type.isUnknown()) {
         throw new Error("Trying to cast to Unknown");
      } else if (type == this) {
         return v;
      } else if ((this.isLong() || this instanceof FloatingPointType) && type.isIntegralType()) {
         Value v = b.newCastExpr(this.asImmediate(b, v), this.typeInt().getSootType(), location);
         return this.typeInt().emitCastTo(b, v, type, location);
      } else if (type instanceof NumericType) {
         return b.newCastExpr(this.asImmediate(b, v), type.getSootType(), location);
      } else {
         return !type.isNumericType() ? this.emitCastTo(b, v, this.boxed(), location) : this.boxed().emitBoxingOperation(b, this.emitCastTo(b, v, type.unboxed(), location), location);
      }
   }

   public NumericType() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
   }

   public NumericType(Modifiers p0, String p1, Opt<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   public NumericType(Modifiers p0, Symbol p1, Opt<Access> p2, List<BodyDecl> p3) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
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

   public void setSuperClassAccessOpt(Opt<Access> opt) {
      this.setChild(opt, 1);
   }

   public boolean hasSuperClassAccess() {
      return this.getSuperClassAccessOpt().getNumChild() != 0;
   }

   public Access getSuperClassAccess() {
      return (Access)this.getSuperClassAccessOpt().getChild(0);
   }

   public void setSuperClassAccess(Access node) {
      this.getSuperClassAccessOpt().setChild(node, 0);
   }

   public Opt<Access> getSuperClassAccessOpt() {
      return (Opt)this.getChild(1);
   }

   public Opt<Access> getSuperClassAccessOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   public void setBodyDeclList(List<BodyDecl> list) {
      this.setChild(list, 2);
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
      List<BodyDecl> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<BodyDecl> getBodyDeclListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   private TypeDecl refined_NumericPromotion_NumericType_binaryNumericPromotion_TypeDecl(TypeDecl type) {
      if (!type.isNumericType()) {
         return this.unknownType();
      } else {
         return this.unaryNumericPromotion().instanceOf(type) ? type : this.unaryNumericPromotion();
      }
   }

   public TypeDecl unaryNumericPromotion() {
      if (this.unaryNumericPromotion_computed) {
         return this.unaryNumericPromotion_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.unaryNumericPromotion_value = this.unaryNumericPromotion_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.unaryNumericPromotion_computed = true;
         }

         return this.unaryNumericPromotion_value;
      }
   }

   private TypeDecl unaryNumericPromotion_compute() {
      return this;
   }

   public TypeDecl binaryNumericPromotion(TypeDecl type) {
      if (this.binaryNumericPromotion_TypeDecl_values == null) {
         this.binaryNumericPromotion_TypeDecl_values = new HashMap(4);
      }

      if (this.binaryNumericPromotion_TypeDecl_values.containsKey(type)) {
         return (TypeDecl)this.binaryNumericPromotion_TypeDecl_values.get(type);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         TypeDecl binaryNumericPromotion_TypeDecl_value = this.binaryNumericPromotion_compute(type);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.binaryNumericPromotion_TypeDecl_values.put(type, binaryNumericPromotion_TypeDecl_value);
         }

         return binaryNumericPromotion_TypeDecl_value;
      }
   }

   private TypeDecl binaryNumericPromotion_compute(TypeDecl type) {
      if (type.isReferenceType()) {
         type = type.unboxed();
      }

      return this.refined_NumericPromotion_NumericType_binaryNumericPromotion_TypeDecl(type);
   }

   public boolean isNumericType() {
      ASTNode$State state = this.state();
      return true;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
