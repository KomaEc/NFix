package soot.JastAddJ;

public class Dims extends ASTNode<ASTNode> implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Dims clone() throws CloneNotSupportedException {
      Dims node = (Dims)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Dims copy() {
      try {
         Dims node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Dims fullCopy() {
      Dims tree = this.copy();
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

   public Dims() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new Opt(), 0);
   }

   public Dims(Opt<Expr> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExprOpt(Opt<Expr> opt) {
      this.setChild(opt, 0);
   }

   public boolean hasExpr() {
      return this.getExprOpt().getNumChild() != 0;
   }

   public Expr getExpr() {
      return (Expr)this.getExprOpt().getChild(0);
   }

   public void setExpr(Expr node) {
      this.getExprOpt().setChild(node, 0);
   }

   public Opt<Expr> getExprOpt() {
      return (Opt)this.getChild(0);
   }

   public Opt<Expr> getExprOptNoTransform() {
      return (Opt)this.getChildNoTransform(0);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
