package soot.JastAddJ;

import soot.Local;
import soot.Value;

public abstract class RelationalExpr extends Binary implements Cloneable {
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

   public RelationalExpr clone() throws CloneNotSupportedException {
      RelationalExpr node = (RelationalExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void typeCheck() {
      if (!this.getLeftOperand().type().isNumericType()) {
         this.error(this.getLeftOperand().type().typeName() + " is not numeric");
      }

      if (!this.getRightOperand().type().isNumericType()) {
         this.error(this.getRightOperand().type().typeName() + " is not numeric");
      }

   }

   public Value eval(Body b) {
      return this.emitBooleanCondition(b);
   }

   public void emitEvalBranch(Body b) {
      b.setLine(this);
      if (this.isTrue()) {
         b.add(b.newGotoStmt(this.true_label(), this));
      } else if (this.isFalse()) {
         b.add(b.newGotoStmt(this.false_label(), this));
      } else {
         TypeDecl type = this.getLeftOperand().type();
         Value left;
         Value right;
         if (type.isNumericType()) {
            type = this.binaryNumericPromotedType();
            left = this.getLeftOperand().type().emitCastTo(b, this.getLeftOperand(), type);
            right = this.getRightOperand().type().emitCastTo(b, this.getRightOperand(), type);
            if (!type.isDouble() && !type.isFloat() && !type.isLong()) {
               b.add(b.newIfStmt(this.comparison(b, left, right), this.true_label(), this));
               b.add(b.newGotoStmt(this.false_label(), this));
            } else {
               Local l;
               if (!type.isDouble() && !type.isFloat()) {
                  l = this.asLocal(b, b.newCmpExpr(this.asImmediate(b, left), this.asImmediate(b, right), this));
               } else if (!(this instanceof GEExpr) && !(this instanceof GTExpr)) {
                  l = this.asLocal(b, b.newCmpgExpr(this.asImmediate(b, left), this.asImmediate(b, right), this));
               } else {
                  l = this.asLocal(b, b.newCmplExpr(this.asImmediate(b, left), this.asImmediate(b, right), this));
               }

               b.add(b.newIfStmt(this.comparisonInv(b, l, BooleanType.emitConstant(false)), this.false_label(), this));
               b.add(b.newGotoStmt(this.true_label(), this));
            }
         } else {
            left = this.getLeftOperand().eval(b);
            right = this.getRightOperand().eval(b);
            b.add(b.newIfStmt(this.comparison(b, left, right), this.true_label(), this));
            b.add(b.newGotoStmt(this.false_label(), this));
         }
      }

   }

   public Value comparison(Body b, Value left, Value right) {
      throw new Error("comparison not supported for " + this.getClass().getName());
   }

   public Value comparisonInv(Body b, Value left, Value right) {
      throw new Error("comparisonInv not supported for " + this.getClass().getName());
   }

   public RelationalExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public RelationalExpr(Expr p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLeftOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getLeftOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getLeftOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setRightOperand(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getRightOperand() {
      return (Expr)this.getChild(1);
   }

   public Expr getRightOperandNoTransform() {
      return (Expr)this.getChildNoTransform(1);
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
      return this.typeBoolean();
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return false;
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      if (caller == this.getRightOperandNoTransform()) {
         return this.false_label();
      } else {
         return caller == this.getLeftOperandNoTransform() ? this.false_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
      }
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      if (caller == this.getRightOperandNoTransform()) {
         return this.true_label();
      } else {
         return caller == this.getLeftOperandNoTransform() ? this.true_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
