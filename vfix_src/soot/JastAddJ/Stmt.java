package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Stmt extends ASTNode<ASTNode> implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean localNum_computed = false;
   protected int localNum_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.localNum_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Stmt clone() throws CloneNotSupportedException {
      Stmt node = (Stmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.localNum_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   void checkUnreachableStmt() {
      if (!this.reachable() && this.reportUnreachable()) {
         this.error("statement is unreachable");
      }

   }

   public void jimplify2(Body b) {
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public abstract boolean modifiedInScope(Variable var1);

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
      return this.isDAbefore(v);
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
      throw new Error("isDUafter in " + this.getClass().getName());
   }

   public boolean declaresVariable(String name) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean continueLabel() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean addsIndentationLevel() {
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
      return true;
   }

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("Can not break at this statement of type " + this.getClass().getName());
   }

   public soot.jimple.Stmt continue_label() {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("Can not continue at this statement");
   }

   public boolean isDAbefore(Variable v) {
      ASTNode$State state = this.state();
      boolean isDAbefore_Variable_value = this.getParent().Define_boolean_isDAbefore(this, (ASTNode)null, v);
      return isDAbefore_Variable_value;
   }

   public boolean isDUbefore(Variable v) {
      ASTNode$State state = this.state();
      boolean isDUbefore_Variable_value = this.getParent().Define_boolean_isDUbefore(this, (ASTNode)null, v);
      return isDUbefore_Variable_value;
   }

   public Collection lookupMethod(String name) {
      ASTNode$State state = this.state();
      Collection lookupMethod_String_value = this.getParent().Define_Collection_lookupMethod(this, (ASTNode)null, name);
      return lookupMethod_String_value;
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public SimpleSet lookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
      return lookupType_String_value;
   }

   public SimpleSet lookupVariable(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
   }

   public BodyDecl enclosingBodyDecl() {
      ASTNode$State state = this.state();
      BodyDecl enclosingBodyDecl_value = this.getParent().Define_BodyDecl_enclosingBodyDecl(this, (ASTNode)null);
      return enclosingBodyDecl_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public boolean reachable() {
      ASTNode$State state = this.state();
      boolean reachable_value = this.getParent().Define_boolean_reachable(this, (ASTNode)null);
      return reachable_value;
   }

   public boolean reportUnreachable() {
      ASTNode$State state = this.state();
      boolean reportUnreachable_value = this.getParent().Define_boolean_reportUnreachable(this, (ASTNode)null);
      return reportUnreachable_value;
   }

   public int localNum() {
      if (this.localNum_computed) {
         return this.localNum_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localNum_value = this.getParent().Define_int_localNum(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localNum_computed = true;
         }

         return this.localNum_value;
      }
   }

   public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.indent();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
