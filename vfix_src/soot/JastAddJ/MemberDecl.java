package soot.JastAddJ;

public abstract class MemberDecl extends BodyDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MemberDecl clone() throws CloneNotSupportedException {
      MemberDecl node = (MemberDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void checkModifiers() {
      if (!this.isSynthetic()) {
         super.checkModifiers();
         if (this.isStatic() && this.hostType().isInnerClass() && !this.isConstant()) {
            this.error("*** Inner classes may not declare static members, unless they are compile-time constant fields");
         }
      }

   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public abstract boolean isStatic();

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
