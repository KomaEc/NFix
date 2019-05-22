package soot.JastAddJ;

import soot.Value;

public class LogNotExpr extends Unary implements Cloneable {
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

   public LogNotExpr clone() throws CloneNotSupportedException {
      LogNotExpr node = (LogNotExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public LogNotExpr copy() {
      try {
         LogNotExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public LogNotExpr fullCopy() {
      LogNotExpr tree = this.copy();
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            ASTNode child = this.children[i];
            if (child != null) {
               child = child.fullCopy();
               tree.setChild(child, i);
            }
         }
      }

      return tree;
   }

   public void typeCheck() {
      if (!this.getOperand().type().isBoolean()) {
         this.error("unary ! only operates on boolean types");
      }

   }

   public Value eval(Body b) {
      return this.emitBooleanCondition(b);
   }

   public void emitEvalBranch(Body b) {
      this.getOperand().emitEvalBranch(b);
   }

   public LogNotExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public LogNotExpr(Expr p0) {
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

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.getOperand().isConstant();
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return Constant.create(!this.getOperand().constant().booleanValue());
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDAafterFalse(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDAafterTrue(v) || this.isTrue();
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafterTrue(v) && this.isDAafterFalse(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDUafterFalse(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getOperand().isDUafterTrue(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafterTrue(v) && this.isDUafterFalse(v);
   }

   public String printPreOp() {
      ASTNode$State state = this.state();
      return "!";
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
      return true;
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return this.getOperand().canBeFalse();
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return this.getOperand().canBeTrue();
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getOperandNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getOperandNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? this.true_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? this.false_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
