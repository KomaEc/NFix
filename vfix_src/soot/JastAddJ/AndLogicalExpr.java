package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class AndLogicalExpr extends LogicalExpr implements Cloneable {
   protected Map isDAafterTrue_Variable_values;
   protected Map isDAafterFalse_Variable_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean next_test_label_computed = false;
   protected soot.jimple.Stmt next_test_label_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafterTrue_Variable_values = null;
      this.isDAafterFalse_Variable_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.next_test_label_computed = false;
      this.next_test_label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AndLogicalExpr clone() throws CloneNotSupportedException {
      AndLogicalExpr node = (AndLogicalExpr)super.clone();
      node.isDAafterTrue_Variable_values = null;
      node.isDAafterFalse_Variable_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.next_test_label_computed = false;
      node.next_test_label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AndLogicalExpr copy() {
      try {
         AndLogicalExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AndLogicalExpr fullCopy() {
      AndLogicalExpr tree = this.copy();
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

   public void emitEvalBranch(Body b) {
      b.setLine(this);
      this.getLeftOperand().emitEvalBranch(b);
      b.addLabel(this.next_test_label());
      if (this.getLeftOperand().canBeTrue()) {
         this.getRightOperand().emitEvalBranch(b);
         if (this.getRightOperand().canBeTrue()) {
            b.add(b.newGotoStmt(this.true_label(), this));
         }
      }

   }

   public AndLogicalExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AndLogicalExpr(Expr p0, Expr p1) {
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

   public Constant constant() {
      ASTNode$State state = this.state();
      return Constant.create(this.left().constant().booleanValue() && this.right().constant().booleanValue());
   }

   public boolean isDAafterTrue(Variable v) {
      if (this.isDAafterTrue_Variable_values == null) {
         this.isDAafterTrue_Variable_values = new HashMap(4);
      }

      if (this.isDAafterTrue_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterTrue_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterTrue_Variable_value = this.isDAafterTrue_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterTrue_Variable_values.put(v, isDAafterTrue_Variable_value);
         }

         return isDAafterTrue_Variable_value;
      }
   }

   private boolean isDAafterTrue_compute(Variable v) {
      return this.getRightOperand().isDAafterTrue(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      if (this.isDAafterFalse_Variable_values == null) {
         this.isDAafterFalse_Variable_values = new HashMap(4);
      }

      if (this.isDAafterFalse_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterFalse_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterFalse_Variable_value = this.isDAafterFalse_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterFalse_Variable_values.put(v, isDAafterFalse_Variable_value);
         }

         return isDAafterFalse_Variable_value;
      }
   }

   private boolean isDAafterFalse_compute(Variable v) {
      return this.getLeftOperand().isDAafterFalse(v) && this.getRightOperand().isDAafterFalse(v) || this.isTrue();
   }

   public boolean isDAafter(Variable v) {
      if (this.isDAafter_Variable_values == null) {
         this.isDAafter_Variable_values = new HashMap(4);
      }

      if (this.isDAafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafter_Variable_value = this.isDAafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, isDAafter_Variable_value);
         }

         return isDAafter_Variable_value;
      }
   }

   private boolean isDAafter_compute(Variable v) {
      return this.isDAafterTrue(v) && this.isDAafterFalse(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getRightOperand().isDUafterTrue(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getLeftOperand().isDUafterFalse(v) && this.getRightOperand().isDUafterFalse(v);
   }

   public boolean isDUafter(Variable v) {
      if (this.isDUafter_Variable_values == null) {
         this.isDUafter_Variable_values = new HashMap(4);
      }

      if (this.isDUafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafter_Variable_value = this.isDUafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, isDUafter_Variable_value);
         }

         return isDUafter_Variable_value;
      }
   }

   private boolean isDUafter_compute(Variable v) {
      return this.isDUafterTrue(v) && this.isDUafterFalse(v);
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " && ";
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return this.getLeftOperand().canBeTrue() && this.getRightOperand().canBeTrue();
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return this.getLeftOperand().canBeFalse() || this.getRightOperand().canBeFalse();
   }

   public soot.jimple.Stmt next_test_label() {
      if (this.next_test_label_computed) {
         return this.next_test_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.next_test_label_value = this.next_test_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.next_test_label_computed = true;
         }

         return this.next_test_label_value;
      }
   }

   private soot.jimple.Stmt next_test_label_compute() {
      return this.newLabel();
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getRightOperandNoTransform()) {
         return this.getLeftOperand().isDAafterTrue(v);
      } else {
         return caller == this.getLeftOperandNoTransform() ? this.isDAbefore(v) : super.Define_boolean_isDAbefore(caller, child, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getRightOperandNoTransform()) {
         return this.getLeftOperand().isDUafterTrue(v);
      } else {
         return caller == this.getLeftOperandNoTransform() ? this.isDUbefore(v) : super.Define_boolean_isDUbefore(caller, child, v);
      }
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
         return caller == this.getLeftOperandNoTransform() ? this.next_test_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
