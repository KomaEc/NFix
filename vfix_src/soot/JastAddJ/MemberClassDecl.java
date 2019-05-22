package soot.JastAddJ;

public class MemberClassDecl extends MemberTypeDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MemberClassDecl clone() throws CloneNotSupportedException {
      MemberClassDecl node = (MemberClassDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MemberClassDecl copy() {
      try {
         MemberClassDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MemberClassDecl fullCopy() {
      MemberClassDecl tree = this.copy();
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
      s.append(this.indent());
      this.getClassDecl().toString(s);
   }

   public MemberClassDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public MemberClassDecl(ClassDecl p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setClassDecl(ClassDecl node) {
      this.setChild(node, 0);
   }

   public ClassDecl getClassDecl() {
      return (ClassDecl)this.getChild(0);
   }

   public ClassDecl getClassDeclNoTransform() {
      return (ClassDecl)this.getChildNoTransform(0);
   }

   public TypeDecl typeDecl() {
      ASTNode$State state = this.state();
      return this.getClassDecl();
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      return caller == this.getClassDeclNoTransform() ? true : this.getParent().Define_boolean_isMemberType(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      return caller == this.getClassDeclNoTransform() ? false : this.getParent().Define_boolean_inStaticContext(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
