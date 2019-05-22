package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public abstract class Case extends Stmt implements Cloneable {
   protected Map isDAbefore_Variable_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean label_computed = false;
   protected soot.jimple.Stmt label_value;
   protected Map bind_Case_values;

   public void flushCache() {
      super.flushCache();
      this.isDAbefore_Variable_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.label_computed = false;
      this.label_value = null;
      this.bind_Case_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Case clone() throws CloneNotSupportedException {
      Case node = (Case)super.clone();
      node.isDAbefore_Variable_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.label_computed = false;
      node.label_value = null;
      node.bind_Case_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void jimplify2(Body b) {
      b.addLabel(this.label());
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public abstract boolean constValue(Case var1);

   public boolean isDAbefore(Variable v) {
      if (this.isDAbefore_Variable_values == null) {
         this.isDAbefore_Variable_values = new HashMap(4);
      }

      if (this.isDAbefore_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAbefore_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAbefore_Variable_value = this.isDAbefore_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, isDAbefore_Variable_value);
         }

         return isDAbefore_Variable_value;
      }
   }

   private boolean isDAbefore_compute(Variable v) {
      return this.getParent().getParent() instanceof Block && ((Block)this.getParent().getParent()).isDAbefore(v) && super.isDAbefore(v);
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

   public boolean isDUbefore(Variable v) {
      ASTNode$State state = this.state();
      return this.getParent().getParent() instanceof Block && ((Block)this.getParent().getParent()).isDUbefore(v) && super.isDUbefore(v);
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

   public boolean reachable() {
      ASTNode$State state = this.state();
      return this.getParent().getParent() instanceof Block && ((Block)this.getParent().getParent()).reachable();
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

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDefaultCase() {
      ASTNode$State state = this.state();
      return false;
   }

   public Case bind(Case c) {
      if (this.bind_Case_values == null) {
         this.bind_Case_values = new HashMap(4);
      }

      if (this.bind_Case_values.containsKey(c)) {
         return (Case)this.bind_Case_values.get(c);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Case bind_Case_value = this.getParent().Define_Case_bind(this, (ASTNode)null, c);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.bind_Case_values.put(c, bind_Case_value);
         }

         return bind_Case_value;
      }
   }

   public TypeDecl switchType() {
      ASTNode$State state = this.state();
      TypeDecl switchType_value = this.getParent().Define_TypeDecl_switchType(this, (ASTNode)null);
      return switchType_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
