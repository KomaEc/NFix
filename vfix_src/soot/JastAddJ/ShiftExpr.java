package soot.JastAddJ;

public abstract class ShiftExpr extends Binary implements Cloneable {
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

   public ShiftExpr clone() throws CloneNotSupportedException {
      ShiftExpr node = (ShiftExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void typeCheck() {
      if (!this.getLeftOperand().type().isIntegralType()) {
         this.error(this.getLeftOperand().type().typeName() + " is not integral");
      }

      if (!this.getRightOperand().type().isIntegralType()) {
         this.error(this.getRightOperand().type().typeName() + " is not integral");
      }

   }

   public ShiftExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ShiftExpr(Expr p0, Expr p1) {
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
      return this.getLeftOperand().type().unaryNumericPromotion();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
