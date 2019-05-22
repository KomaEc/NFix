package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class IfStmt extends Stmt implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed;
   protected boolean canCompleteNormally_value;
   protected boolean else_branch_label_computed;
   protected soot.jimple.Stmt else_branch_label_value;
   protected boolean then_branch_label_computed;
   protected soot.jimple.Stmt then_branch_label_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.else_branch_label_computed = false;
      this.else_branch_label_value = null;
      this.then_branch_label_computed = false;
      this.then_branch_label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public IfStmt clone() throws CloneNotSupportedException {
      IfStmt node = (IfStmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.else_branch_label_computed = false;
      node.else_branch_label_value = null;
      node.then_branch_label_computed = false;
      node.then_branch_label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public IfStmt copy() {
      try {
         IfStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public IfStmt fullCopy() {
      IfStmt tree = this.copy();
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

   public IfStmt(Expr cond, Stmt thenBranch) {
      this(cond, thenBranch, new Opt());
   }

   public IfStmt(Expr cond, Stmt thenBranch, Stmt elseBranch) {
      this(cond, thenBranch, new Opt(elseBranch));
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("if(");
      this.getCondition().toString(s);
      s.append(") ");
      this.getThen().toString(s);
      if (this.hasElse()) {
         s.append(this.indent());
         s.append("else ");
         this.getElse().toString(s);
      }

   }

   public void typeCheck() {
      TypeDecl cond = this.getCondition().type();
      if (!cond.isBoolean()) {
         this.error("the type of \"" + this.getCondition() + "\" is " + cond.name() + " which is not boolean");
      }

   }

   public void jimplify2(Body b) {
      soot.jimple.Stmt endBranch = this.newLabel();
      if (this.getCondition().isConstant()) {
         if (this.getCondition().isTrue()) {
            this.getThen().jimplify2(b);
         } else if (this.getCondition().isFalse() && this.hasElse()) {
            this.getElse().jimplify2(b);
         }
      } else {
         soot.jimple.Stmt elseBranch = this.else_branch_label();
         soot.jimple.Stmt thenBranch = this.then_branch_label();
         this.getCondition().emitEvalBranch(b);
         b.addLabel(thenBranch);
         this.getThen().jimplify2(b);
         if (this.getThen().canCompleteNormally() && this.hasElse()) {
            b.setLine(this);
            b.add(b.newGotoStmt(endBranch, this));
         }

         b.addLabel(elseBranch);
         if (this.hasElse()) {
            this.getElse().jimplify2(b);
         }
      }

      if (this.getThen().canCompleteNormally() && this.hasElse()) {
         b.addLabel(endBranch);
      }

   }

   public IfStmt() {
      this.canCompleteNormally_computed = false;
      this.else_branch_label_computed = false;
      this.then_branch_label_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 2);
   }

   public IfStmt(Expr p0, Stmt p1, Opt<Stmt> p2) {
      this.canCompleteNormally_computed = false;
      this.else_branch_label_computed = false;
      this.then_branch_label_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setCondition(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getCondition() {
      return (Expr)this.getChild(0);
   }

   public Expr getConditionNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setThen(Stmt node) {
      this.setChild(node, 1);
   }

   public Stmt getThen() {
      return (Stmt)this.getChild(1);
   }

   public Stmt getThenNoTransform() {
      return (Stmt)this.getChildNoTransform(1);
   }

   public void setElseOpt(Opt<Stmt> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasElse() {
      return this.getElseOpt().getNumChild() != 0;
   }

   public Stmt getElse() {
      return (Stmt)this.getElseOpt().getChild(0);
   }

   public void setElse(Stmt node) {
      this.getElseOpt().setChild(node, 0);
   }

   public Opt<Stmt> getElseOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Stmt> getElseOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
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
      return this.hasElse() ? this.getThen().isDAafter(v) && this.getElse().isDAafter(v) : this.getThen().isDAafter(v) && this.getCondition().isDAafterFalse(v);
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
      return this.hasElse() ? this.getThen().isDUafter(v) && this.getElse().isDUafter(v) : this.getThen().isDUafter(v) && this.getCondition().isDUafterFalse(v);
   }

   public boolean canCompleteNormally() {
      if (this.canCompleteNormally_computed) {
         return this.canCompleteNormally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.canCompleteNormally_value = this.canCompleteNormally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
         }

         return this.canCompleteNormally_value;
      }
   }

   private boolean canCompleteNormally_compute() {
      return this.reachable() && !this.hasElse() || this.getThen().canCompleteNormally() || this.hasElse() && this.getElse().canCompleteNormally();
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return true;
   }

   public soot.jimple.Stmt else_branch_label() {
      if (this.else_branch_label_computed) {
         return this.else_branch_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.else_branch_label_value = this.else_branch_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.else_branch_label_computed = true;
         }

         return this.else_branch_label_value;
      }
   }

   private soot.jimple.Stmt else_branch_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt then_branch_label() {
      if (this.then_branch_label_computed) {
         return this.then_branch_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.then_branch_label_value = this.then_branch_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.then_branch_label_computed = true;
         }

         return this.then_branch_label_value;
      }
   }

   private soot.jimple.Stmt then_branch_label_compute() {
      return this.newLabel();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      if (this.getThen().modifiedInScope(var)) {
         return true;
      } else {
         return this.hasElse() && this.getElse().modifiedInScope(var);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getElseOptNoTransform()) {
         return this.getCondition().isDAafterFalse(v);
      } else if (caller == this.getThenNoTransform()) {
         return this.getCondition().isDAafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getElseOptNoTransform()) {
         return this.getCondition().isDUafterFalse(v);
      } else if (caller == this.getThenNoTransform()) {
         return this.getCondition().isDUafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller == this.getElseOptNoTransform()) {
         return this.reachable();
      } else {
         return caller == this.getThenNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      if (caller == this.getElseOptNoTransform()) {
         return this.reachable();
      } else {
         return caller == this.getThenNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
      }
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionNoTransform() ? this.else_branch_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionNoTransform() ? this.then_branch_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
