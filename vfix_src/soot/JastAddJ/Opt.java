package soot.JastAddJ;

public class Opt<T extends ASTNode> extends ASTNode<T> implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Opt<T> clone() throws CloneNotSupportedException {
      Opt node = (Opt)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Opt<T> copy() {
      try {
         Opt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public Opt<T> fullCopy() {
      Opt tree = this.copy();
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

   public Opt() {
   }

   public void init$Children() {
   }

   public Opt(T opt) {
      this.setChild(opt, 0);
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return this.getParent().definesLabel();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
