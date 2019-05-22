package soot.JastAddJ;

public class Dot extends AbstractDot implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Dot clone() throws CloneNotSupportedException {
      Dot node = (Dot)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Dot copy() {
      try {
         Dot node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Dot fullCopy() {
      Dot tree = this.copy();
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

   public Dot lastDot() {
      Dot node;
      for(node = this; node.getRightNoTransform() instanceof Dot; node = (Dot)node.getRightNoTransform()) {
      }

      return node;
   }

   public Dot qualifiesAccess(Access access) {
      Dot lastDot = this.lastDot();
      Expr l = lastDot.getRightNoTransform();
      Dot dot = new Dot(lastDot.getRightNoTransform(), access);
      dot.setStart(l.getStart());
      dot.setEnd(access.getEnd());
      lastDot.setRight(dot);
      return this;
   }

   private Access qualifyTailWith(Access expr) {
      if (this.getRight() instanceof AbstractDot) {
         AbstractDot dot = (AbstractDot)this.getRight();
         return expr.qualifiesAccess(dot.getRight());
      } else {
         return expr;
      }
   }

   public Access extractLast() {
      return this.lastDot().getRightNoTransform();
   }

   public void replaceLast(Access access) {
      this.lastDot().setRight(access);
   }

   public Dot() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public Dot(Expr p0, Access p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return true;
   }

   public void setLeft(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getLeft() {
      return (Expr)this.getChild(0);
   }

   public Expr getLeftNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setRight(Access node) {
      this.setChild(node, 1);
   }

   public Access getRight() {
      return (Access)this.getChild(1);
   }

   public Access getRightNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   public ASTNode rewriteTo() {
      Access result;
      if (!this.duringSyntacticClassification() && this.leftSide().isPackageAccess() && this.rightSide().isPackageAccess()) {
         ++this.state().duringNameResolution;
         result = this.rewriteRule0();
         --this.state().duringNameResolution;
         return result;
      } else if (!this.duringSyntacticClassification() && this.leftSide().isPackageAccess() && !((Access)this.leftSide()).hasPrevExpr() && this.rightSide() instanceof TypeAccess) {
         ++this.state().duringNameResolution;
         result = this.rewriteRule1();
         --this.state().duringNameResolution;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private Access rewriteRule0() {
      PackageAccess left = (PackageAccess)this.leftSide();
      PackageAccess right = (PackageAccess)this.rightSide();
      left.setPackage(left.getPackage() + "." + right.getPackage());
      left.setEnd(right.end());
      return this.qualifyTailWith(left);
   }

   private Access rewriteRule1() {
      PackageAccess left = (PackageAccess)this.leftSide();
      TypeAccess right = (TypeAccess)this.rightSide();
      right.setPackage(left.getPackage());
      right.setStart(left.start());
      return this.qualifyTailWith(right);
   }
}
