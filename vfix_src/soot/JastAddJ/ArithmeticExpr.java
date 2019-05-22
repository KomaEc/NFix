package soot.JastAddJ;

public abstract class ArithmeticExpr extends Binary implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArithmeticExpr clone() throws CloneNotSupportedException {
      ArithmeticExpr node = (ArithmeticExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArithmeticExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ArithmeticExpr(Expr p0, Expr p1) {
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
