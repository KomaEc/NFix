package soot.JastAddJ;

public class DefaultCase extends Case implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public DefaultCase clone() throws CloneNotSupportedException {
      DefaultCase node = (DefaultCase)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public DefaultCase copy() {
      try {
         DefaultCase node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public DefaultCase fullCopy() {
      DefaultCase tree = this.copy();
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

   public void nameCheck() {
      if (this.bind(this) != this) {
         this.error("only one default case statement allowed");
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("default:");
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public boolean constValue(Case c) {
      ASTNode$State state = this.state();
      return c instanceof DefaultCase;
   }

   public boolean isDefaultCase() {
      ASTNode$State state = this.state();
      return true;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
