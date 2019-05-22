package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LabeledStmt extends BranchTargetStmt implements Cloneable {
   protected String tokenString_Label;
   public int Labelstart;
   public int Labelend;
   protected Map targetOf_ContinueStmt_values;
   protected Map targetOf_BreakStmt_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean label_computed = false;
   protected soot.jimple.Stmt label_value;
   protected boolean end_label_computed = false;
   protected soot.jimple.Stmt end_label_value;
   protected Map lookupLabel_String_values;

   public void flushCache() {
      super.flushCache();
      this.targetOf_ContinueStmt_values = null;
      this.targetOf_BreakStmt_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.label_computed = false;
      this.label_value = null;
      this.end_label_computed = false;
      this.end_label_value = null;
      this.lookupLabel_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public LabeledStmt clone() throws CloneNotSupportedException {
      LabeledStmt node = (LabeledStmt)super.clone();
      node.targetOf_ContinueStmt_values = null;
      node.targetOf_BreakStmt_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.label_computed = false;
      node.label_value = null;
      node.end_label_computed = false;
      node.end_label_value = null;
      node.lookupLabel_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public LabeledStmt copy() {
      try {
         LabeledStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public LabeledStmt fullCopy() {
      LabeledStmt tree = this.copy();
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

   public void nameCheck() {
      LabeledStmt stmt = this.lookupLabel(this.getLabel());
      if (stmt != null && stmt.enclosingBodyDecl() == this.enclosingBodyDecl()) {
         this.error("Labels can not shadow labels in the same member");
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append(this.getLabel() + ":");
      this.getStmt().toString(s);
   }

   public void jimplify2(Body b) {
      b.setLine(this);
      b.addLabel(this.label());
      this.getStmt().jimplify2(b);
      b.addLabel(this.end_label());
   }

   public LabeledStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public LabeledStmt(String p0, Stmt p1) {
      this.setLabel(p0);
      this.setChild(p1, 0);
   }

   public LabeledStmt(Symbol p0, Stmt p1) {
      this.setLabel(p0);
      this.setChild(p1, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLabel(String value) {
      this.tokenString_Label = value;
   }

   public void setLabel(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setLabel is only valid for String lexemes");
      } else {
         this.tokenString_Label = (String)symbol.value;
         this.Labelstart = symbol.getStart();
         this.Labelend = symbol.getEnd();
      }
   }

   public String getLabel() {
      return this.tokenString_Label != null ? this.tokenString_Label : "";
   }

   public void setStmt(Stmt node) {
      this.setChild(node, 0);
   }

   public Stmt getStmt() {
      return (Stmt)this.getChild(0);
   }

   public Stmt getStmtNoTransform() {
      return (Stmt)this.getChildNoTransform(0);
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
      return stmt.hasLabel() && stmt.getLabel().equals(this.getLabel());
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
      return stmt.hasLabel() && stmt.getLabel().equals(this.getLabel());
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
      if (!this.getStmt().isDAafter(v)) {
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
      if (!this.getStmt().isDUafter(v)) {
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
      return this.getStmt().canCompleteNormally() || this.reachableBreak();
   }

   public soot.jimple.Stmt label() {
      if (this.label_computed) {
         return this.label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_value = this.label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_computed = true;
         }

         return this.label_value;
      }
   }

   private soot.jimple.Stmt label_compute() {
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
      return this.getStmt().continue_label();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getStmt().modifiedInScope(var);
   }

   public LabeledStmt lookupLabel(String name) {
      if (this.lookupLabel_String_values == null) {
         this.lookupLabel_String_values = new HashMap(4);
      }

      if (this.lookupLabel_String_values.containsKey(name)) {
         return (LabeledStmt)this.lookupLabel_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         LabeledStmt lookupLabel_String_value = this.getParent().Define_LabeledStmt_lookupLabel(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupLabel_String_values.put(name, lookupLabel_String_value);
         }

         return lookupLabel_String_value;
      }
   }

   public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getStmtNoTransform()) {
         return name.equals(this.getLabel()) ? this : this.lookupLabel(name);
      } else {
         return this.getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getStmtNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getStmtNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
