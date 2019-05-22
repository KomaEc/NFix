package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WhileStmt extends BranchTargetStmt implements Cloneable {
   protected Map targetOf_ContinueStmt_values;
   protected Map targetOf_BreakStmt_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map isDUbeforeCondition_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean cond_label_computed = false;
   protected soot.jimple.Stmt cond_label_value;
   protected boolean end_label_computed = false;
   protected soot.jimple.Stmt end_label_value;
   protected boolean stmt_label_computed = false;
   protected soot.jimple.Stmt stmt_label_value;

   public void flushCache() {
      super.flushCache();
      this.targetOf_ContinueStmt_values = null;
      this.targetOf_BreakStmt_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.isDUbeforeCondition_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.cond_label_computed = false;
      this.cond_label_value = null;
      this.end_label_computed = false;
      this.end_label_value = null;
      this.stmt_label_computed = false;
      this.stmt_label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public WhileStmt clone() throws CloneNotSupportedException {
      WhileStmt node = (WhileStmt)super.clone();
      node.targetOf_ContinueStmt_values = null;
      node.targetOf_BreakStmt_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.isDUbeforeCondition_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.cond_label_computed = false;
      node.cond_label_value = null;
      node.end_label_computed = false;
      node.end_label_value = null;
      node.stmt_label_computed = false;
      node.stmt_label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public WhileStmt copy() {
      try {
         WhileStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public WhileStmt fullCopy() {
      WhileStmt tree = this.copy();
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

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("while(");
      this.getCondition().toString(s);
      s.append(")");
      this.getStmt().toString(s);
   }

   public void typeCheck() {
      TypeDecl cond = this.getCondition().type();
      if (!cond.isBoolean()) {
         this.error("the type of \"" + this.getCondition() + "\" is " + cond.name() + " which is not boolean");
      }

   }

   public void jimplify2(Body b) {
      b.addLabel(this.cond_label());
      this.getCondition().emitEvalBranch(b);
      b.addLabel(this.stmt_label());
      if (this.getCondition().canBeTrue()) {
         this.getStmt().jimplify2(b);
         if (this.getStmt().canCompleteNormally()) {
            b.setLine(this);
            b.add(b.newGotoStmt(this.cond_label(), this));
         }
      }

      if (this.canCompleteNormally()) {
         b.addLabel(this.end_label());
      }

   }

   public WhileStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public WhileStmt(Expr p0, Stmt p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setStmt(Stmt node) {
      this.setChild(node, 1);
   }

   public Stmt getStmt() {
      return (Stmt)this.getChild(1);
   }

   public Stmt getStmtNoTransform() {
      return (Stmt)this.getChildNoTransform(1);
   }

   public boolean targetOf(ContinueStmt stmt) {
      if (this.targetOf_ContinueStmt_values == null) {
         this.targetOf_ContinueStmt_values = new HashMap(4);
      }

      if (this.targetOf_ContinueStmt_values.containsKey(stmt)) {
         return (Boolean)this.targetOf_ContinueStmt_values.get(stmt);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean targetOf_ContinueStmt_value = this.targetOf_compute(stmt);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetOf_ContinueStmt_values.put(stmt, targetOf_ContinueStmt_value);
         }

         return targetOf_ContinueStmt_value;
      }
   }

   private boolean targetOf_compute(ContinueStmt stmt) {
      return !stmt.hasLabel();
   }

   public boolean targetOf(BreakStmt stmt) {
      if (this.targetOf_BreakStmt_values == null) {
         this.targetOf_BreakStmt_values = new HashMap(4);
      }

      if (this.targetOf_BreakStmt_values.containsKey(stmt)) {
         return (Boolean)this.targetOf_BreakStmt_values.get(stmt);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean targetOf_BreakStmt_value = this.targetOf_compute(stmt);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetOf_BreakStmt_values.put(stmt, targetOf_BreakStmt_value);
         }

         return targetOf_BreakStmt_value;
      }
   }

   private boolean targetOf_compute(BreakStmt stmt) {
      return !stmt.hasLabel();
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
      if (!this.getCondition().isDAafterFalse(v)) {
         return false;
      } else {
         Iterator iter = this.targetBreaks().iterator();

         BreakStmt stmt;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            stmt = (BreakStmt)iter.next();
         } while(stmt.isDAafterReachedFinallyBlocks(v));

         return false;
      }
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
      if (!this.isDUbeforeCondition(v)) {
         return false;
      } else if (!this.getCondition().isDUafterFalse(v)) {
         return false;
      } else {
         Iterator iter = this.targetBreaks().iterator();

         BreakStmt stmt;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            stmt = (BreakStmt)iter.next();
         } while(stmt.isDUafterReachedFinallyBlocks(v));

         return false;
      }
   }

   public boolean isDUbeforeCondition(Variable v) {
      if (this.isDUbeforeCondition_Variable_values == null) {
         this.isDUbeforeCondition_Variable_values = new HashMap(4);
      }

      ASTNode$State.CircularValue _value;
      if (this.isDUbeforeCondition_Variable_values.containsKey(v)) {
         Object _o = this.isDUbeforeCondition_Variable_values.get(v);
         if (!(_o instanceof ASTNode$State.CircularValue)) {
            return (Boolean)_o;
         }

         _value = (ASTNode$State.CircularValue)_o;
      } else {
         _value = new ASTNode$State.CircularValue();
         this.isDUbeforeCondition_Variable_values.put(v, _value);
         _value.value = true;
      }

      ASTNode$State state = this.state();
      if (state.IN_CIRCLE) {
         if (!(new Integer(state.CIRCLE_INDEX)).equals(_value.visited)) {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            boolean new_isDUbeforeCondition_Variable_value = this.isDUbeforeCondition_compute(v);
            if (state.RESET_CYCLE) {
               this.isDUbeforeCondition_Variable_values.remove(v);
            } else if (new_isDUbeforeCondition_Variable_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_isDUbeforeCondition_Variable_value;
            }

            return new_isDUbeforeCondition_Variable_value;
         } else {
            return (Boolean)_value.value;
         }
      } else {
         state.IN_CIRCLE = true;
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();

         boolean new_isDUbeforeCondition_Variable_value;
         do {
            _value.visited = new Integer(state.CIRCLE_INDEX);
            state.CHANGE = false;
            new_isDUbeforeCondition_Variable_value = this.isDUbeforeCondition_compute(v);
            if (new_isDUbeforeCondition_Variable_value != (Boolean)_value.value) {
               state.CHANGE = true;
               _value.value = new_isDUbeforeCondition_Variable_value;
            }

            ++state.CIRCLE_INDEX;
         } while(state.CHANGE);

         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUbeforeCondition_Variable_values.put(v, new_isDUbeforeCondition_Variable_value);
         } else {
            this.isDUbeforeCondition_Variable_values.remove(v);
            state.RESET_CYCLE = true;
            this.isDUbeforeCondition_compute(v);
            state.RESET_CYCLE = false;
         }

         state.IN_CIRCLE = false;
         return new_isDUbeforeCondition_Variable_value;
      }
   }

   private boolean isDUbeforeCondition_compute(Variable v) {
      if (!this.isDUbefore(v)) {
         return false;
      } else if (!this.getStmt().isDUafter(v)) {
         return false;
      } else {
         Iterator iter = this.targetContinues().iterator();

         ContinueStmt stmt;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            stmt = (ContinueStmt)iter.next();
         } while(stmt.isDUafterReachedFinallyBlocks(v));

         return false;
      }
   }

   public boolean continueLabel() {
      ASTNode$State state = this.state();
      return true;
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
      return this.reachable() && (!this.getCondition().isConstant() || !this.getCondition().isTrue()) || this.reachableBreak();
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return true;
   }

   public soot.jimple.Stmt cond_label() {
      if (this.cond_label_computed) {
         return this.cond_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.cond_label_value = this.cond_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.cond_label_computed = true;
         }

         return this.cond_label_value;
      }
   }

   private soot.jimple.Stmt cond_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt end_label() {
      if (this.end_label_computed) {
         return this.end_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.end_label_value = this.end_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.end_label_computed = true;
         }

         return this.end_label_value;
      }
   }

   private soot.jimple.Stmt end_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt stmt_label() {
      if (this.stmt_label_computed) {
         return this.stmt_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.stmt_label_value = this.stmt_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.stmt_label_computed = true;
         }

         return this.stmt_label_value;
      }
   }

   private soot.jimple.Stmt stmt_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      return this.end_label();
   }

   public soot.jimple.Stmt continue_label() {
      ASTNode$State state = this.state();
      return this.cond_label();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getStmt().modifiedInScope(var);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtNoTransform()) {
         return this.getCondition().isDAafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtNoTransform()) {
         return this.getCondition().isDUafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDUbeforeCondition(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? true : this.getParent().Define_boolean_insideLoop(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller != this.getStmtNoTransform()) {
         return this.getParent().Define_boolean_reachable(this, caller);
      } else {
         return this.reachable() && !this.getCondition().isFalse();
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionNoTransform() ? this.end_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionNoTransform() ? this.stmt_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
