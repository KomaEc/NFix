package soot.JastAddJ;

public abstract class PrimaryExpr extends Expr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public PrimaryExpr clone() throws CloneNotSupportedException {
      PrimaryExpr node = (PrimaryExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
