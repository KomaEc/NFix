package soot.JastAddJ;

public abstract class AssignBitwiseExpr extends AssignExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssignBitwiseExpr clone() throws CloneNotSupportedException {
      AssignBitwiseExpr node = (AssignBitwiseExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void typeCheck() {
      TypeDecl source = this.sourceType();
      TypeDecl dest = this.getDest().type();
      if (source.isIntegralType() && dest.isIntegralType()) {
         super.typeCheck();
      } else if (source.isBoolean() && dest.isBoolean()) {
         super.typeCheck();
      } else {
         this.error("Operator only operates on integral and boolean types");
      }

   }

   public AssignBitwiseExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignBitwiseExpr(Expr p0, Expr p1) {
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
