package soot.JastAddJ;

import beaver.Symbol;

public class BoundFieldAccess extends VarAccess implements Cloneable {
   protected FieldDeclaration tokenFieldDeclaration_FieldDeclaration;
   protected boolean decl_computed;
   protected Variable decl_value;

   public void flushCache() {
      super.flushCache();
      this.decl_computed = false;
      this.decl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BoundFieldAccess clone() throws CloneNotSupportedException {
      BoundFieldAccess node = (BoundFieldAccess)super.clone();
      node.decl_computed = false;
      node.decl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BoundFieldAccess copy() {
      try {
         BoundFieldAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BoundFieldAccess fullCopy() {
      BoundFieldAccess tree = this.copy();
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

   public BoundFieldAccess(FieldDeclaration f) {
      this(f.name(), f);
   }

   public boolean isExactVarAccess() {
      return false;
   }

   public BoundFieldAccess() {
      this.decl_computed = false;
   }

   public void init$Children() {
   }

   public BoundFieldAccess(String p0, FieldDeclaration p1) {
      this.decl_computed = false;
      this.setID(p0);
      this.setFieldDeclaration(p1);
   }

   public BoundFieldAccess(Symbol p0, FieldDeclaration p1) {
      this.decl_computed = false;
      this.setID(p0);
      this.setFieldDeclaration(p1);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   public void setFieldDeclaration(FieldDeclaration value) {
      this.tokenFieldDeclaration_FieldDeclaration = value;
   }

   public FieldDeclaration getFieldDeclaration() {
      return this.tokenFieldDeclaration_FieldDeclaration;
   }

   public Variable decl() {
      if (this.decl_computed) {
         return this.decl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decl_value = this.decl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decl_computed = true;
         }

         return this.decl_value;
      }
   }

   private Variable decl_compute() {
      return this.getFieldDeclaration();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
