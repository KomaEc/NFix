package soot.JastAddJ;

public class StaticImportOnDemandDecl extends StaticImportDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public StaticImportOnDemandDecl clone() throws CloneNotSupportedException {
      StaticImportOnDemandDecl node = (StaticImportOnDemandDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public StaticImportOnDemandDecl copy() {
      try {
         StaticImportOnDemandDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public StaticImportOnDemandDecl fullCopy() {
      StaticImportOnDemandDecl tree = this.copy();
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
      s.append("import static ");
      this.getAccess().toString(s);
      s.append(".*;\n");
   }

   public StaticImportOnDemandDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public StaticImportOnDemandDecl(Access p0) {
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
      ASTNode$State state = this.state();
      return this.getAccess().type();
   }

   public boolean isOnDemand() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
