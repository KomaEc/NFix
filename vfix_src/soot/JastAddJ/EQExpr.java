package soot.JastAddJ;

import soot.Value;

public class EQExpr extends EqualityExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EQExpr clone() throws CloneNotSupportedException {
      EQExpr node = (EQExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EQExpr copy() {
      try {
         EQExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EQExpr fullCopy() {
      EQExpr tree = this.copy();
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

   public Value comparison(Body b, Value left, Value right) {
      return b.newEqExpr(this.asImmediate(b, left), this.asImmediate(b, right), this);
   }

   public Value comparisonInv(Body b, Value left, Value right) {
      return b.newNeExpr(this.asImmediate(b, left), this.asImmediate(b, right), this);
   }

   public EQExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public EQExpr(Expr p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLeftOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getLeftOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getLeftOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setRightOperand(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getRightOperand() {
      return (Expr)this.getChild(1);
   }

   public Expr getRightOperandNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return Constant.create(this.binaryNumericPromotedType().eqIsTrue(this.left(), this.right()));
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " == ";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
