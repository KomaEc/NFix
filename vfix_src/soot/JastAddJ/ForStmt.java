package soot.JastAddJ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ForStmt extends BranchTargetStmt implements Cloneable, VariableScope {
   protected Map targetOf_ContinueStmt_values;
   protected Map targetOf_BreakStmt_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map isDUbeforeCondition_Variable_values;
   protected Map localLookup_String_values;
   protected Map localVariableDeclaration_String_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean cond_label_computed = false;
   protected soot.jimple.Stmt cond_label_value;
   protected boolean begin_label_computed = false;
   protected soot.jimple.Stmt begin_label_value;
   protected boolean update_label_computed = false;
   protected soot.jimple.Stmt update_label_value;
   protected boolean end_label_computed = false;
   protected soot.jimple.Stmt end_label_value;
   protected Map lookupVariable_String_values;

   public void flushCache() {
      super.flushCache();
      this.targetOf_ContinueStmt_values = null;
      this.targetOf_BreakStmt_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.isDUbeforeCondition_Variable_values = null;
      this.localLookup_String_values = null;
      this.localVariableDeclaration_String_values = null;
      this.canCompleteNormally_computed = false;
      this.cond_label_computed = false;
      this.cond_label_value = null;
      this.begin_label_computed = false;
      this.begin_label_value = null;
      this.update_label_computed = false;
      this.update_label_value = null;
      this.end_label_computed = false;
      this.end_label_value = null;
      this.lookupVariable_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ForStmt clone() throws CloneNotSupportedException {
      ForStmt node = (ForStmt)super.clone();
      node.targetOf_ContinueStmt_values = null;
      node.targetOf_BreakStmt_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.isDUbeforeCondition_Variable_values = null;
      node.localLookup_String_values = null;
      node.localVariableDeclaration_String_values = null;
      node.canCompleteNormally_computed = false;
      node.cond_label_computed = false;
      node.cond_label_value = null;
      node.begin_label_computed = false;
      node.begin_label_value = null;
      node.update_label_computed = false;
      node.update_label_value = null;
      node.end_label_computed = false;
      node.end_label_value = null;
      node.lookupVariable_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ForStmt copy() {
      try {
         ForStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ForStmt fullCopy() {
      ForStmt tree = this.copy();
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
      s.append("for(");
      int i;
      ExprStmt stmt;
      if (this.getNumInitStmt() > 0) {
         if (this.getInitStmt(0) instanceof VariableDeclaration) {
            int minDimension = Integer.MAX_VALUE;

            for(i = 0; i < this.getNumInitStmt(); ++i) {
               VariableDeclaration v = (VariableDeclaration)this.getInitStmt(i);
               minDimension = Math.min(minDimension, v.type().dimension());
            }

            VariableDeclaration v = (VariableDeclaration)this.getInitStmt(0);
            v.getModifiers().toString(s);
            s.append(v.type().elementType().typeName());

            int i;
            for(i = minDimension; i > 0; --i) {
               s.append("[]");
            }

            for(i = 0; i < this.getNumInitStmt(); ++i) {
               if (i != 0) {
                  s.append(",");
               }

               v = (VariableDeclaration)this.getInitStmt(i);
               s.append(" " + v.name());

               for(int j = v.type().dimension() - minDimension; j > 0; --j) {
                  s.append("[]");
               }

               if (v.hasInit()) {
                  s.append(" = ");
                  v.getInit().toString(s);
               }
            }
         } else {
            if (!(this.getInitStmt(0) instanceof ExprStmt)) {
               throw new Error("Unexpected initializer in for loop: " + this.getInitStmt(0));
            }

            stmt = (ExprStmt)this.getInitStmt(0);
            stmt.getExpr().toString(s);

            for(i = 1; i < this.getNumInitStmt(); ++i) {
               s.append(", ");
               stmt = (ExprStmt)this.getInitStmt(i);
               stmt.getExpr().toString(s);
            }
         }
      }

      s.append("; ");
      if (this.hasCondition()) {
         this.getCondition().toString(s);
      }

      s.append("; ");
      if (this.getNumUpdateStmt() > 0) {
         stmt = (ExprStmt)this.getUpdateStmt(0);
         stmt.getExpr().toString(s);

         for(i = 1; i < this.getNumUpdateStmt(); ++i) {
            s.append(", ");
            stmt = (ExprStmt)this.getUpdateStmt(i);
            stmt.getExpr().toString(s);
         }
      }

      s.append(") ");
      this.getStmt().toString(s);
   }

   public void typeCheck() {
      if (this.hasCondition()) {
         TypeDecl cond = this.getCondition().type();
         if (!cond.isBoolean()) {
            this.error("the type of \"" + this.getCondition() + "\" is " + cond.name() + " which is not boolean");
         }
      }

   }

   public void jimplify2(Body b) {
      int i;
      for(i = 0; i < this.getNumInitStmt(); ++i) {
         this.getInitStmt(i).jimplify2(b);
      }

      b.addLabel(this.cond_label());
      this.getCondition().emitEvalBranch(b);
      if (this.getCondition().canBeTrue()) {
         b.addLabel(this.begin_label());
         this.getStmt().jimplify2(b);
         b.addLabel(this.update_label());

         for(i = 0; i < this.getNumUpdateStmt(); ++i) {
            this.getUpdateStmt(i).jimplify2(b);
         }

         b.setLine(this);
         b.add(b.newGotoStmt(this.cond_label(), this));
      }

      if (this.canCompleteNormally()) {
         b.addLabel(this.end_label());
      }

   }

   public ForStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 0);
      this.setChild(new Opt(), 1);
      this.setChild(new List(), 2);
   }

   public ForStmt(List<Stmt> p0, Opt<Expr> p1, List<Stmt> p2, Stmt p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
      this.setChild(p3, 3);
   }

   protected int numChildren() {
      return 4;
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public void setInitStmtList(List<Stmt> list) {
      this.setChild(list, 0);
   }

   public int getNumInitStmt() {
      return this.getInitStmtList().getNumChild();
   }

   public int getNumInitStmtNoTransform() {
      return this.getInitStmtListNoTransform().getNumChildNoTransform();
   }

   public Stmt getInitStmt(int i) {
      return (Stmt)this.getInitStmtList().getChild(i);
   }

   public void addInitStmt(Stmt node) {
      List<Stmt> list = this.parent != null && state != null ? this.getInitStmtList() : this.getInitStmtListNoTransform();
      list.addChild(node);
   }

   public void addInitStmtNoTransform(Stmt node) {
      List<Stmt> list = this.getInitStmtListNoTransform();
      list.addChild(node);
   }

   public void setInitStmt(Stmt node, int i) {
      List<Stmt> list = this.getInitStmtList();
      list.setChild(node, i);
   }

   public List<Stmt> getInitStmts() {
      return this.getInitStmtList();
   }

   public List<Stmt> getInitStmtsNoTransform() {
      return this.getInitStmtListNoTransform();
   }

   public List<Stmt> getInitStmtList() {
      List<Stmt> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Stmt> getInitStmtListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public void setConditionOpt(Opt<Expr> opt) {
      this.setChild(opt, 1);
   }

   public boolean hasCondition() {
      return this.getConditionOpt().getNumChild() != 0;
   }

   public Expr getCondition() {
      return (Expr)this.getConditionOpt().getChild(0);
   }

   public void setCondition(Expr node) {
      this.getConditionOpt().setChild(node, 0);
   }

   public Opt<Expr> getConditionOpt() {
      return (Opt)this.getChild(1);
   }

   public Opt<Expr> getConditionOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   public void setUpdateStmtList(List<Stmt> list) {
      this.setChild(list, 2);
   }

   public int getNumUpdateStmt() {
      return this.getUpdateStmtList().getNumChild();
   }

   public int getNumUpdateStmtNoTransform() {
      return this.getUpdateStmtListNoTransform().getNumChildNoTransform();
   }

   public Stmt getUpdateStmt(int i) {
      return (Stmt)this.getUpdateStmtList().getChild(i);
   }

   public void addUpdateStmt(Stmt node) {
      List<Stmt> list = this.parent != null && state != null ? this.getUpdateStmtList() : this.getUpdateStmtListNoTransform();
      list.addChild(node);
   }

   public void addUpdateStmtNoTransform(Stmt node) {
      List<Stmt> list = this.getUpdateStmtListNoTransform();
      list.addChild(node);
   }

   public void setUpdateStmt(Stmt node, int i) {
      List<Stmt> list = this.getUpdateStmtList();
      list.setChild(node, i);
   }

   public List<Stmt> getUpdateStmts() {
      return this.getUpdateStmtList();
   }

   public List<Stmt> getUpdateStmtsNoTransform() {
      return this.getUpdateStmtListNoTransform();
   }

   public List<Stmt> getUpdateStmtList() {
      List<Stmt> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Stmt> getUpdateStmtListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setStmt(Stmt node) {
      this.setChild(node, 3);
   }

   public Stmt getStmt() {
      return (Stmt)this.getChild(3);
   }

   public Stmt getStmtNoTransform() {
      return (Stmt)this.getChildNoTransform(3);
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
      if (this.hasCondition() && !this.getCondition().isDAafterFalse(v)) {
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

   public boolean isDAafterInitialization(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumInitStmt() == 0 ? this.isDAbefore(v) : this.getInitStmt(this.getNumInitStmt() - 1).isDAafter(v);
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
      } else if (this.hasCondition() && !this.getCondition().isDUafterFalse(v)) {
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

   public boolean isDUafterInit(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumInitStmt() == 0 ? this.isDUbefore(v) : this.getInitStmt(this.getNumInitStmt() - 1).isDUafter(v);
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
      if (!this.isDUafterInit(v)) {
         return false;
      } else {
         return this.isDUafterUpdate(v);
      }
   }

   public boolean isDUafterUpdate(Variable v) {
      ASTNode$State state = this.state();
      if (!this.isDUbeforeCondition(v)) {
         return false;
      } else if (this.getNumUpdateStmt() > 0) {
         return this.getUpdateStmt(this.getNumUpdateStmt() - 1).isDUafter(v);
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

   public SimpleSet localLookup(String name) {
      if (this.localLookup_String_values == null) {
         this.localLookup_String_values = new HashMap(4);
      }

      if (this.localLookup_String_values.containsKey(name)) {
         return (SimpleSet)this.localLookup_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet localLookup_String_value = this.localLookup_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localLookup_String_values.put(name, localLookup_String_value);
         }

         return localLookup_String_value;
      }
   }

   private SimpleSet localLookup_compute(String name) {
      VariableDeclaration v = this.localVariableDeclaration(name);
      return (SimpleSet)(v != null ? v : this.lookupVariable(name));
   }

   public VariableDeclaration localVariableDeclaration(String name) {
      if (this.localVariableDeclaration_String_values == null) {
         this.localVariableDeclaration_String_values = new HashMap(4);
      }

      if (this.localVariableDeclaration_String_values.containsKey(name)) {
         return (VariableDeclaration)this.localVariableDeclaration_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         VariableDeclaration localVariableDeclaration_String_value = this.localVariableDeclaration_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localVariableDeclaration_String_values.put(name, localVariableDeclaration_String_value);
         }

         return localVariableDeclaration_String_value;
      }
   }

   private VariableDeclaration localVariableDeclaration_compute(String name) {
      for(int i = 0; i < this.getNumInitStmt(); ++i) {
         if (this.getInitStmt(i).declaresVariable(name)) {
            return (VariableDeclaration)this.getInitStmt(i);
         }
      }

      return null;
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
      return this.reachable() && this.hasCondition() && (!this.getCondition().isConstant() || !this.getCondition().isTrue()) || this.reachableBreak();
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

   public soot.jimple.Stmt begin_label() {
      if (this.begin_label_computed) {
         return this.begin_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.begin_label_value = this.begin_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.begin_label_computed = true;
         }

         return this.begin_label_value;
      }
   }

   private soot.jimple.Stmt begin_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt update_label() {
      if (this.update_label_computed) {
         return this.update_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.update_label_value = this.update_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.update_label_computed = true;
         }

         return this.update_label_value;
      }
   }

   private soot.jimple.Stmt update_label_compute() {
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

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      return this.end_label();
   }

   public soot.jimple.Stmt continue_label() {
      ASTNode$State state = this.state();
      return this.update_label();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      Iterator var3 = this.getInitStmtList().iterator();

      Stmt stmt;
      do {
         if (!var3.hasNext()) {
            var3 = this.getUpdateStmtList().iterator();

            do {
               if (!var3.hasNext()) {
                  return this.getStmt().modifiedInScope(var);
               }

               stmt = (Stmt)var3.next();
            } while(!stmt.modifiedInScope(var));

            return true;
         }

         stmt = (Stmt)var3.next();
      } while(!stmt.modifiedInScope(var));

      return true;
   }

   public SimpleSet lookupVariable(String name) {
      if (this.lookupVariable_String_values == null) {
         this.lookupVariable_String_values = new HashMap(4);
      }

      if (this.lookupVariable_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupVariable_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
         }

         return lookupVariable_String_value;
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getUpdateStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         if (!this.getStmt().isDAafter(v)) {
            return false;
         } else {
            Iterator iter = this.targetContinues().iterator();

            ContinueStmt stmt;
            do {
               if (!iter.hasNext()) {
                  return true;
               }

               stmt = (ContinueStmt)iter.next();
            } while(stmt.isDAafterReachedFinallyBlocks(v));

            return false;
         }
      } else if (caller == this.getStmtNoTransform()) {
         if (this.hasCondition() && this.getCondition().isDAafterTrue(v)) {
            return true;
         } else {
            return !this.hasCondition() && this.isDAafterInitialization(v);
         }
      } else if (caller == this.getConditionOptNoTransform()) {
         return this.isDAafterInitialization(v);
      } else if (caller == this.getInitStmtListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return i == 0 ? this.isDAbefore(v) : this.getInitStmt(i - 1).isDAafter(v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      int childIndex;
      if (caller == this.getUpdateStmtListNoTransform()) {
         childIndex = caller.getIndexOfChild(child);
         if (!this.isDUbeforeCondition(v)) {
            return false;
         } else if (childIndex == 0) {
            if (!this.getStmt().isDUafter(v)) {
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
         } else {
            return this.getUpdateStmt(childIndex - 1).isDUafter(v);
         }
      } else if (caller != this.getStmtNoTransform()) {
         if (caller == this.getConditionOptNoTransform()) {
            return this.isDUbeforeCondition(v);
         } else if (caller == this.getInitStmtListNoTransform()) {
            childIndex = caller.getIndexOfChild(child);
            return childIndex == 0 ? this.isDUbefore(v) : this.getInitStmt(childIndex - 1).isDUafter(v);
         } else {
            return this.getParent().Define_boolean_isDUbefore(this, caller, v);
         }
      } else {
         boolean var10000;
         label83: {
            if (this.isDUbeforeCondition(v)) {
               if (this.hasCondition()) {
                  if (this.getCondition().isDUafterTrue(v)) {
                     break label83;
                  }
               } else if (this.isDUafterInit(v)) {
                  break label83;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getStmtNoTransform()) {
         return this.localLookup(name);
      } else if (caller == this.getUpdateStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.localLookup(name);
      } else if (caller == this.getConditionOptNoTransform()) {
         return this.localLookup(name);
      } else if (caller == this.getInitStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.localLookup(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtNoTransform()) {
         return this;
      } else if (caller == this.getInitStmtListNoTransform()) {
         caller.getIndexOfChild(child);
         return this;
      } else {
         return this.getParent().Define_VariableScope_outerScope(this, caller);
      }
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? true : this.getParent().Define_boolean_insideLoop(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller != this.getStmtNoTransform()) {
         return this.getParent().Define_boolean_reachable(this, caller);
      } else {
         return this.reachable() && (!this.hasCondition() || !this.getCondition().isConstant() || !this.getCondition().isFalse());
      }
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? this.reachable() : this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionOptNoTransform() ? this.end_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      return caller == this.getConditionOptNoTransform() ? this.begin_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
   }

   public ASTNode rewriteTo() {
      if (!this.hasCondition()) {
         ++this.state().duringDU;
         ASTNode result = this.rewriteRule0();
         --this.state().duringDU;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private ForStmt rewriteRule0() {
      this.setCondition(new BooleanLiteral("true"));
      return this;
   }
}
