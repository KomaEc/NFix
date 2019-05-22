package soot.JastAddJ;

import soot.Value;

public class AssignMinusExpr extends AssignAdditiveExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssignMinusExpr clone() throws CloneNotSupportedException {
      AssignMinusExpr node = (AssignMinusExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AssignMinusExpr copy() {
      try {
         AssignMinusExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AssignMinusExpr fullCopy() {
      AssignMinusExpr tree = this.copy();
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

   public void typeCheck() {
      if (this.sourceType().isBoolean() || this.getDest().type().isBoolean()) {
         this.error("Operator - does not operate on boolean types");
      }

      super.typeCheck();
   }

   public Value createAssignOp(Body b, Value fst, Value snd) {
      return b.newSubExpr(this.asImmediate(b, fst), this.asImmediate(b, snd), this);
   }

   public AssignMinusExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignMinusExpr(Expr p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setDest(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getDest() {
      return (Expr)this.getChild(0);
   }

   public Expr getDestNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setSource(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getSource() {
      return (Expr)this.getChild(1);
   }

   public Expr getSourceNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " -= ";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
