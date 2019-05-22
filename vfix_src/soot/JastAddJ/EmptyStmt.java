package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class EmptyStmt extends Stmt implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EmptyStmt clone() throws CloneNotSupportedException {
      EmptyStmt node = (EmptyStmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EmptyStmt copy() {
      try {
         EmptyStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EmptyStmt fullCopy() {
      EmptyStmt tree = this.copy();
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
      s.append(";");
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
      return this.isDUbefore(v);
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
      return this.reachable();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
