package soot.JastAddJ;

public abstract class AbstractWildcard extends Access implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AbstractWildcard clone() throws CloneNotSupportedException {
      AbstractWildcard node = (AbstractWildcard)super.clone();
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
