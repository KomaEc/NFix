package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import soot.Value;

public class SwitchStmt extends BranchTargetStmt implements Cloneable {
   protected Map targetOf_ContinueStmt_values;
   protected Map targetOf_BreakStmt_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean defaultCase_computed = false;
   protected DefaultCase defaultCase_value;
   protected boolean end_label_computed = false;
   protected soot.jimple.Stmt end_label_value;
   protected boolean typeInt_computed = false;
   protected TypeDecl typeInt_value;
   protected boolean typeLong_computed = false;
   protected TypeDecl typeLong_value;

   public void flushCache() {
      super.flushCache();
      this.targetOf_ContinueStmt_values = null;
      this.targetOf_BreakStmt_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.defaultCase_computed = false;
      this.defaultCase_value = null;
      this.end_label_computed = false;
      this.end_label_value = null;
      this.typeInt_computed = false;
      this.typeInt_value = null;
      this.typeLong_computed = false;
      this.typeLong_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public SwitchStmt clone() throws CloneNotSupportedException {
      SwitchStmt node = (SwitchStmt)super.clone();
      node.targetOf_ContinueStmt_values = null;
      node.targetOf_BreakStmt_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.defaultCase_computed = false;
      node.defaultCase_value = null;
      node.end_label_computed = false;
      node.end_label_value = null;
      node.typeInt_computed = false;
      node.typeInt_value = null;
      node.typeLong_computed = false;
      node.typeLong_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SwitchStmt copy() {
      try {
         SwitchStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public SwitchStmt fullCopy() {
      SwitchStmt tree = this.copy();
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
      s.append("switch (");
      this.getExpr().toString(s);
      s.append(")");
      this.getBlock().toString(s);
   }

   public void jimplify2(Body b) {
      soot.jimple.Stmt cond_label = this.newLabel();
      soot.jimple.Stmt switch_label = this.newLabel();
      b.setLine(this);
      b.add(b.newGotoStmt(cond_label, this));
      this.getBlock().jimplify2(b);
      if (this.canCompleteNormally()) {
         b.setLine(this);
         b.add(b.newGotoStmt(this.end_label(), this));
      }

      b.addLabel(cond_label);
      Value expr = this.asImmediate(b, this.getExpr().eval(b));
      TreeMap map = new TreeMap();

      for(int i = 0; i < this.getBlock().getNumStmt(); ++i) {
         if (this.getBlock().getStmt(i) instanceof ConstCase) {
            ConstCase ca = (ConstCase)this.getBlock().getStmt(i);
            map.put(new Integer(ca.getValue().constant().intValue()), ca);
         }
      }

      long low = map.isEmpty() ? 0L : (long)(Integer)map.firstKey();
      long high = map.isEmpty() ? 0L : (long)(Integer)map.lastKey();
      long tableSwitchSize = 8L + (high - low + 1L) * 4L;
      long lookupSwitchSize = 4L + (long)map.size() * 8L;
      b.addLabel(switch_label);
      soot.jimple.Stmt defaultStmt = this.defaultCase() != null ? this.defaultCase().label() : this.end_label();
      ArrayList targets;
      ConstCase ca;
      if (tableSwitchSize < lookupSwitchSize) {
         targets = new ArrayList();

         for(long i = low; i <= high; ++i) {
            ca = (ConstCase)map.get(new Integer((int)i));
            if (ca != null) {
               targets.add(ca.label());
            } else {
               targets.add(defaultStmt);
            }
         }

         b.setLine(this);
         b.add(b.newTableSwitchStmt(expr, (int)low, (int)high, targets, defaultStmt, this));
      } else {
         targets = new ArrayList();
         ArrayList values = new ArrayList();
         Iterator iter = map.values().iterator();

         while(iter.hasNext()) {
            ca = (ConstCase)iter.next();
            targets.add(ca.label());
            values.add(IntType.emitConstant(ca.getValue().constant().intValue()));
         }

         b.setLine(this);
         b.add(b.newLookupSwitchStmt(expr, values, targets, defaultStmt, this));
      }

      b.addLabel(this.end_label());
   }

   public void transformation() {
      if (this.getExpr().type().isEnumDecl()) {
         TypeDecl type = this.getExpr().type();
         this.hostType().createEnumArray(type);
         this.hostType().createEnumMethod(type);
         this.setExpr(this.hostType().createEnumMethod(type).createBoundAccess(new List()).qualifiesAccess(new ArrayAccess(this.getExpr().qualifiesAccess(new MethodAccess("ordinal", new List())))));
      }

      super.transformation();
   }

   public SwitchStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public SwitchStmt(Expr p0, Block p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExpr(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(0);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setBlock(Block node) {
      this.setChild(node, 1);
   }

   public Block getBlock() {
      return (Block)this.getChild(1);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(1);
   }

   public void refined_Enums_SwitchStmt_typeCheck() {
      TypeDecl type = this.getExpr().type();
      if ((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl()) {
         this.error("Switch expression must be of char, byte, short, int, or enum type");
      }

   }

   public void typeCheck() {
      TypeDecl type = this.getExpr().type();
      if ((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl() && !type.isString()) {
         this.error("Switch expression must be of type char, byte, short, int, enum, or string");
      }

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
      return false;
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
      if (this.noDefaultLabel() && !this.getExpr().isDAafter(v)) {
         return false;
      } else if (this.switchLabelEndsBlock() && !this.getExpr().isDAafter(v)) {
         return false;
      } else if (!this.assignedAfterLastStmt(v)) {
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

   public boolean assignedAfterLastStmt(Variable v) {
      ASTNode$State state = this.state();
      return this.getBlock().isDAafter(v);
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
      if (this.noDefaultLabel() && !this.getExpr().isDUafter(v)) {
         return false;
      } else if (this.switchLabelEndsBlock() && !this.getExpr().isDUafter(v)) {
         return false;
      } else if (!this.unassignedAfterLastStmt(v)) {
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

   public boolean unassignedAfterLastStmt(Variable v) {
      ASTNode$State state = this.state();
      return this.getBlock().isDUafter(v);
   }

   public boolean switchLabelEndsBlock() {
      ASTNode$State state = this.state();
      return this.getBlock().getNumStmt() > 0 && this.getBlock().getStmt(this.getBlock().getNumStmt() - 1) instanceof ConstCase;
   }

   public boolean lastStmtCanCompleteNormally() {
      ASTNode$State state = this.state();
      return this.getBlock().canCompleteNormally();
   }

   public boolean noStmts() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getBlock().getNumStmt(); ++i) {
         if (!(this.getBlock().getStmt(i) instanceof Case)) {
            return false;
         }
      }

      return true;
   }

   public boolean noStmtsAfterLastLabel() {
      ASTNode$State state = this.state();
      return this.getBlock().getNumStmt() > 0 && this.getBlock().getStmt(this.getBlock().getNumStmt() - 1) instanceof Case;
   }

   public boolean noDefaultLabel() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getBlock().getNumStmt(); ++i) {
         if (this.getBlock().getStmt(i) instanceof DefaultCase) {
            return false;
         }
      }

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
      return this.lastStmtCanCompleteNormally() || this.noStmts() || this.noStmtsAfterLastLabel() || this.noDefaultLabel() || this.reachableBreak();
   }

   public DefaultCase defaultCase() {
      if (this.defaultCase_computed) {
         return this.defaultCase_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.defaultCase_value = this.defaultCase_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.defaultCase_computed = true;
         }

         return this.defaultCase_value;
      }
   }

   private DefaultCase defaultCase_compute() {
      for(int i = 0; i < this.getBlock().getNumStmt(); ++i) {
         if (this.getBlock().getStmt(i) instanceof DefaultCase) {
            return (DefaultCase)this.getBlock().getStmt(i);
         }
      }

      return null;
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

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      return this.end_label();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getBlock().modifiedInScope(var);
   }

   public TypeDecl typeInt() {
      if (this.typeInt_computed) {
         return this.typeInt_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeInt_value = this.getParent().Define_TypeDecl_typeInt(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeInt_computed = true;
         }

         return this.typeInt_value;
      }
   }

   public TypeDecl typeLong() {
      if (this.typeLong_computed) {
         return this.typeLong_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeLong_value = this.getParent().Define_TypeDecl_typeLong(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeLong_computed = true;
         }

         return this.typeLong_value;
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.getExpr().isDAafter(v);
      } else if (caller == this.getExprNoTransform()) {
         if (((ASTNode)v).isDescendantTo(this)) {
            return false;
         } else {
            boolean result = this.isDAbefore(v);
            return result;
         }
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getBlockNoTransform()) {
         return this.getExpr().isDUafter(v);
      } else {
         return caller == this.getExprNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? true : this.getParent().Define_boolean_insideSwitch(this, caller);
   }

   public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
      if (caller == this.getBlockNoTransform()) {
         Block b = this.getBlock();

         for(int i = 0; i < b.getNumStmt(); ++i) {
            if (b.getStmt(i) instanceof Case && ((Case)b.getStmt(i)).constValue(c)) {
               return (Case)b.getStmt(i);
            }
         }

         return null;
      } else {
         return this.getParent().Define_Case_bind(this, caller, c);
      }
   }

   public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.getExpr().type() : this.getParent().Define_TypeDecl_switchType(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
