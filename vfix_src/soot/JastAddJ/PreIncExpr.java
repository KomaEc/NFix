package soot.JastAddJ;

import soot.Value;

public class PreIncExpr extends Unary implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PreIncExpr clone() throws CloneNotSupportedException {
      PreIncExpr node = (PreIncExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PreIncExpr copy() {
      try {
         PreIncExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PreIncExpr fullCopy() {
      PreIncExpr tree = this.copy();
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

   public void definiteAssignment() {
      if (this.getOperand().isVariable()) {
         Variable v = this.getOperand().varDecl();
         if (v != null && v.isFinal()) {
            this.error("++ and -- can not be applied to final variable " + v);
         }
      }

   }

   protected boolean checkDUeverywhere(Variable v) {
      return this.getOperand().isVariable() && this.getOperand().varDecl() == v && !this.isDAbefore(v) ? false : super.checkDUeverywhere(v);
   }

   public void typeCheck() {
      if (!this.getOperand().isVariable()) {
         this.error("prefix increment expression only work on variables");
      } else if (!this.getOperand().type().isNumericType()) {
         this.error("unary increment only operates on numeric types");
      }

   }

   public Value eval(Body b) {
      return this.emitPrefix(b, 1);
   }

   public PreIncExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public PreIncExpr(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public String printPreOp() {
      ASTNode$State state = this.state();
      return "++";
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? true : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
      return caller == this.getOperandNoTransform() ? true : this.getParent().Define_boolean_isIncOrDec(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
