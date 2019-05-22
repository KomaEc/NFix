package soot.JastAddJ;

public class WildcardSuper extends AbstractWildcard implements Cloneable {
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

   public WildcardSuper clone() throws CloneNotSupportedException {
      WildcardSuper node = (WildcardSuper)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public WildcardSuper copy() {
      try {
         WildcardSuper node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public WildcardSuper fullCopy() {
      WildcardSuper tree = this.copy();
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

   public void toString(StringBuffer s) {
      s.append("? super ");
      this.getAccess().toString(s);
   }

   public WildcardSuper() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public WildcardSuper(Access p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getAccess() {
      return (Access)this.getChild(0);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
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
      return this.lookupWildcardSuper(this.getAccess().type());
   }

   public TypeDecl lookupWildcardSuper(TypeDecl typeDecl) {
      ASTNode$State state = this.state();
      TypeDecl lookupWildcardSuper_TypeDecl_value = this.getParent().Define_TypeDecl_lookupWildcardSuper(this, (ASTNode)null, typeDecl);
      return lookupWildcardSuper_TypeDecl_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
