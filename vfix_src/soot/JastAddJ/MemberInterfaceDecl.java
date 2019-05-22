package soot.JastAddJ;

public class MemberInterfaceDecl extends MemberTypeDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MemberInterfaceDecl clone() throws CloneNotSupportedException {
      MemberInterfaceDecl node = (MemberInterfaceDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MemberInterfaceDecl copy() {
      try {
         MemberInterfaceDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MemberInterfaceDecl fullCopy() {
      MemberInterfaceDecl tree = this.copy();
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

   public void checkModifiers() {
      super.checkModifiers();
      if (this.hostType().isInnerClass()) {
         this.error("*** Inner classes may not declare member interfaces");
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getInterfaceDecl().toString(s);
   }

   public MemberInterfaceDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public MemberInterfaceDecl(InterfaceDecl p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setInterfaceDecl(InterfaceDecl node) {
      this.setChild(node, 0);
   }

   public InterfaceDecl getInterfaceDecl() {
      return (InterfaceDecl)this.getChild(0);
   }

   public InterfaceDecl getInterfaceDeclNoTransform() {
      return (InterfaceDecl)this.getChildNoTransform(0);
   }

   public TypeDecl typeDecl() {
      ASTNode$State state = this.state();
      return this.getInterfaceDecl();
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      return caller == this.getInterfaceDeclNoTransform() ? true : this.getParent().Define_boolean_isMemberType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
