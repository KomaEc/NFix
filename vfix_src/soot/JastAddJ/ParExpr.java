package soot.JastAddJ;

import soot.Value;

public class ParExpr extends PrimaryExpr implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParExpr clone() throws CloneNotSupportedException {
      ParExpr node = (ParExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParExpr copy() {
      try {
         ParExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParExpr fullCopy() {
      ParExpr tree = this.copy();
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
      s.append("(");
      this.getExpr().toString(s);
      s.append(")");
   }

   public void typeCheck() {
      if (this.getExpr().isTypeAccess()) {
         this.error("" + this.getExpr() + " is a type and may not be used in parenthesized expression");
      }

   }

   public void emitEvalBranch(Body b) {
      this.getExpr().emitEvalBranch(b);
   }

   public Value eval(Body b) {
      return this.getExpr().eval(b);
   }

   public ParExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ParExpr(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
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

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.getExpr().constant();
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.getExpr().isConstant();
   }

   public Variable varDecl() {
      ASTNode$State state = this.state();
      return this.getExpr().varDecl();
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafterTrue(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafterFalse(v) || this.isTrue();
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafter(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafterTrue(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafterFalse(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafter(v);
   }

   public boolean isSuperAccess() {
      ASTNode$State state = this.state();
      return this.getExpr().isSuperAccess();
   }

   public boolean isThisAccess() {
      ASTNode$State state = this.state();
      return this.getExpr().isThisAccess();
   }

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      return this.getExpr().isTypeAccess() ? this.unknownType() : this.getExpr().type();
   }

   public boolean isVariable() {
      ASTNode$State state = this.state();
      return this.getExpr().isVariable();
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return this.getExpr().staticContextQualifier();
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return this.getParent().definesLabel();
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return this.getExpr().canBeTrue();
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return this.getExpr().canBeFalse();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
