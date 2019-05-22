package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class StaticInitializer extends BodyDecl implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map handlesException_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.handlesException_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public StaticInitializer clone() throws CloneNotSupportedException {
      StaticInitializer node = (StaticInitializer)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.handlesException_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public StaticInitializer copy() {
      try {
         StaticInitializer node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public StaticInitializer fullCopy() {
      StaticInitializer tree = this.copy();
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

   public void checkModifiers() {
      super.checkModifiers();
      if (this.hostType().isInnerClass()) {
         this.error("*** Inner classes may not declare static initializers");
      }

   }

   public void toString(StringBuffer s) {
      if (this.getBlock().getNumStmt() != 0) {
         s.append(this.indent());
         s.append("static ");
         this.getBlock().toString(s);
      }
   }

   void checkUnreachableStmt() {
      if (!this.getBlock().canCompleteNormally()) {
         this.error("static initializer in " + this.hostType().fullName() + " can not complete normally");
      }

   }

   public StaticInitializer() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public StaticInitializer(Block p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setBlock(Block node) {
      this.setChild(node, 0);
   }

   public Block getBlock() {
      return (Block)this.getChild(0);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(0);
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
      return this.getBlock().isDUafter(v);
   }

   public boolean visibleTypeParameters() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean handlesException(TypeDecl exceptionType) {
      if (this.handlesException_TypeDecl_values == null) {
         this.handlesException_TypeDecl_values = new HashMap(4);
      }

      if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.handlesException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, handlesException_TypeDecl_value);
         }

         return handlesException_TypeDecl_value;
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getBlockNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
      if (caller == this.getBlockNoTransform()) {
         return this.hostType().isAnonymous() ? this.handlesException(exceptionType) : !exceptionType.isUncheckedException();
      } else {
         return this.getParent().Define_boolean_handlesException(this, caller, exceptionType);
      }
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? null : this.getParent().Define_TypeDecl_enclosingInstance(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? true : this.getParent().Define_boolean_inStaticContext(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? true : this.getParent().Define_boolean_reachable(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
