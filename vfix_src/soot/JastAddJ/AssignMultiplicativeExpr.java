package soot.JastAddJ;

public abstract class AssignMultiplicativeExpr extends AssignExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssignMultiplicativeExpr clone() throws CloneNotSupportedException {
      AssignMultiplicativeExpr node = (AssignMultiplicativeExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void typeCheck() {
      if (this.sourceType().isBoolean() || this.getDest().type().isBoolean()) {
         this.error("Multiplicative operators do not operate on boolean types");
      }

      super.typeCheck();
   }

   public AssignMultiplicativeExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignMultiplicativeExpr(Expr p0, Expr p1) {
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
