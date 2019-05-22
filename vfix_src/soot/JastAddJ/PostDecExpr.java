package soot.JastAddJ;

import soot.Value;

public class PostDecExpr extends PostfixExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PostDecExpr clone() throws CloneNotSupportedException {
      PostDecExpr node = (PostDecExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public PostDecExpr copy() {
      try {
         PostDecExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public PostDecExpr fullCopy() {
      PostDecExpr tree = this.copy();
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

   public Value eval(Body b) {
      return this.emitPostfix(b, -1);
   }

   public PostDecExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public PostDecExpr(Expr p0) {
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

   public String printPostOp() {
      ASTNode$State state = this.state();
      return "--";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
