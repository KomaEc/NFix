package soot.JastAddJ;

import soot.Local;
import soot.Value;

public abstract class Unary extends Expr implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Unary clone() throws CloneNotSupportedException {
      Unary node = (Unary)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void toString(StringBuffer s) {
      s.append(this.printPreOp());
      this.getOperand().toString(s);
      s.append(this.printPostOp());
   }

   public Value eval(Body b) {
      return super.eval(b);
   }

   public Value emitPostfix(Body b, int constant) {
      Value lvalue = this.getOperand().eval(b);
      Value v = lvalue instanceof Local ? lvalue : (Value)lvalue.clone();
      TypeDecl type = this.getOperand().type().binaryNumericPromotion(this.typeInt());
      Value value = b.newTemp(this.getOperand().type().emitCastTo(b, v, type, this.getOperand()));
      Value rvalue = this.typeInt().emitCastTo(b, IntType.emitConstant(constant), type, this);
      Value sum = this.asRValue(b, type.emitCastTo(b, b.newAddExpr(this.asImmediate(b, value), this.asImmediate(b, rvalue), this), this.getOperand().type(), this));
      this.getOperand().emitStore(b, lvalue, sum, this);
      return value;
   }

   public Value emitPrefix(Body b, int constant) {
      Value lvalue = this.getOperand().eval(b);
      Value v = lvalue instanceof Local ? lvalue : (Value)lvalue.clone();
      TypeDecl type = this.getOperand().type().binaryNumericPromotion(this.typeInt());
      Value value = this.getOperand().type().emitCastTo(b, v, type, this.getOperand());
      Value rvalue = this.typeInt().emitCastTo(b, IntType.emitConstant(constant), type, this);
      Value result = this.asLocal(b, type.emitCastTo(b, b.newAddExpr(this.asImmediate(b, value), this.asImmediate(b, rvalue), this), this.getOperand().type(), this));
      this.getOperand().emitStore(b, lvalue, result, this);
      return result;
   }

   public Unary() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public Unary(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDUafter(v);
   }

   public String printPostOp() {
      ASTNode$State state = this.state();
      return "";
   }

   public String printPreOp() {
      ASTNode$State state = this.state();
      return "";
   }

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      return this.getOperand().type();
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
