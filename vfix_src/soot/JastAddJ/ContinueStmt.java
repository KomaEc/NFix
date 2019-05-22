package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContinueStmt extends Stmt implements Cloneable {
   protected String tokenString_Label;
   public int Labelstart;
   public int Labelend;
   protected boolean targetStmt_computed = false;
   protected Stmt targetStmt_value;
   protected boolean finallyList_computed = false;
   protected ArrayList finallyList_value;
   protected Map isDAafter_Variable_values;
   protected Map isDUafterReachedFinallyBlocks_Variable_values;
   protected Map isDAafterReachedFinallyBlocks_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean inSynchronizedBlock_computed = false;
   protected boolean inSynchronizedBlock_value;
   protected Map lookupLabel_String_values;

   public void flushCache() {
      super.flushCache();
      this.targetStmt_computed = false;
      this.targetStmt_value = null;
      this.finallyList_computed = false;
      this.finallyList_value = null;
      this.isDAafter_Variable_values = null;
      this.isDUafterReachedFinallyBlocks_Variable_values = null;
      this.isDAafterReachedFinallyBlocks_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.inSynchronizedBlock_computed = false;
      this.lookupLabel_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ContinueStmt clone() throws CloneNotSupportedException {
      ContinueStmt node = (ContinueStmt)super.clone();
      node.targetStmt_computed = false;
      node.targetStmt_value = null;
      node.finallyList_computed = false;
      node.finallyList_value = null;
      node.isDAafter_Variable_values = null;
      node.isDUafterReachedFinallyBlocks_Variable_values = null;
      node.isDAafterReachedFinallyBlocks_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.inSynchronizedBlock_computed = false;
      node.lookupLabel_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ContinueStmt copy() {
      try {
         ContinueStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ContinueStmt fullCopy() {
      ContinueStmt tree = this.copy();
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

   public void collectBranches(Collection c) {
      c.add(this);
   }

   public void nameCheck() {
      if (!this.insideLoop()) {
         this.error("continue outside loop");
      } else if (this.hasLabel()) {
         LabeledStmt label = this.lookupLabel(this.getLabel());
         if (label == null) {
            this.error("labeled continue must have visible matching label");
         } else if (!label.getStmt().continueLabel()) {
            this.error(this.getLabel() + " is not a loop label");
         }
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("continue ");
      if (this.hasLabel()) {
         s.append(this.getLabel());
      }

      s.append(";");
   }

   public void jimplify2(Body b) {
      ArrayList list = this.exceptionRanges();
      if (!this.inSynchronizedBlock()) {
         this.endExceptionRange(b, list);
      }

      Iterator iter = this.finallyList().iterator();

      while(iter.hasNext()) {
         FinallyHost stmt = (FinallyHost)iter.next();
         stmt.emitFinallyCode(b);
      }

      if (this.inSynchronizedBlock()) {
         this.endExceptionRange(b, list);
      }

      b.setLine(this);
      b.add(b.newGotoStmt(this.targetStmt().continue_label(), this));
      this.beginExceptionRange(b, list);
   }

   public ContinueStmt() {
   }

   public void init$Children() {
   }

   public ContinueStmt(String p0) {
      this.setLabel(p0);
   }

   public ContinueStmt(Symbol p0) {
      this.setLabel(p0);
   }

   protected int numChildren() {
      return 0;
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

   public boolean hasLabel() {
      ASTNode$State state = this.state();
      return !this.getLabel().equals("");
   }

   public Stmt targetStmt() {
      if (this.targetStmt_computed) {
         return this.targetStmt_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.targetStmt_value = this.targetStmt_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetStmt_computed = true;
         }

         return this.targetStmt_value;
      }
   }

   private Stmt targetStmt_compute() {
      return this.branchTarget(this);
   }

   public ArrayList finallyList() {
      if (this.finallyList_computed) {
         return this.finallyList_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.finallyList_value = this.finallyList_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.finallyList_computed = true;
         }

         return this.finallyList_value;
      }
   }

   private ArrayList finallyList_compute() {
      ArrayList list = new ArrayList();
      this.collectFinally(this, list);
      return list;
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
      return true;
   }

   public boolean isDUafterReachedFinallyBlocks(Variable v) {
      if (this.isDUafterReachedFinallyBlocks_Variable_values == null) {
         this.isDUafterReachedFinallyBlocks_Variable_values = new HashMap(4);
      }

      if (this.isDUafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafterReachedFinallyBlocks_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafterReachedFinallyBlocks_Variable_value = this.isDUafterReachedFinallyBlocks_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafterReachedFinallyBlocks_Variable_values.put(v, isDUafterReachedFinallyBlocks_Variable_value);
         }

         return isDUafterReachedFinallyBlocks_Variable_value;
      }
   }

   private boolean isDUafterReachedFinallyBlocks_compute(Variable v) {
      if (!this.isDUbefore(v) && this.finallyList().isEmpty()) {
         return false;
      } else {
         Iterator iter = this.finallyList().iterator();

         FinallyHost f;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            f = (FinallyHost)iter.next();
         } while(f.isDUafterFinally(v));

         return false;
      }
   }

   public boolean isDAafterReachedFinallyBlocks(Variable v) {
      if (this.isDAafterReachedFinallyBlocks_Variable_values == null) {
         this.isDAafterReachedFinallyBlocks_Variable_values = new HashMap(4);
      }

      if (this.isDAafterReachedFinallyBlocks_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterReachedFinallyBlocks_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterReachedFinallyBlocks_Variable_value = this.isDAafterReachedFinallyBlocks_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterReachedFinallyBlocks_Variable_values.put(v, isDAafterReachedFinallyBlocks_Variable_value);
         }

         return isDAafterReachedFinallyBlocks_Variable_value;
      }
   }

   private boolean isDAafterReachedFinallyBlocks_compute(Variable v) {
      if (this.isDAbefore(v)) {
         return true;
      } else if (this.finallyList().isEmpty()) {
         return false;
      } else {
         Iterator iter = this.finallyList().iterator();

         FinallyHost f;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            f = (FinallyHost)iter.next();
         } while(f.isDAafterFinally(v));

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
      return false;
   }

   public boolean inSynchronizedBlock() {
      if (this.inSynchronizedBlock_computed) {
         return this.inSynchronizedBlock_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.inSynchronizedBlock_value = this.inSynchronizedBlock_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.inSynchronizedBlock_computed = true;
         }

         return this.inSynchronizedBlock_value;
      }
   }

   private boolean inSynchronizedBlock_compute() {
      return !this.finallyList().isEmpty() && this.finallyList().iterator().next() instanceof SynchronizedStmt;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
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

   public boolean insideLoop() {
      ASTNode$State state = this.state();
      boolean insideLoop_value = this.getParent().Define_boolean_insideLoop(this, (ASTNode)null);
      return insideLoop_value;
   }

   public ArrayList exceptionRanges() {
      ASTNode$State state = this.state();
      ArrayList exceptionRanges_value = this.getParent().Define_ArrayList_exceptionRanges(this, (ASTNode)null);
      return exceptionRanges_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
