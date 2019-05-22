package soot.JastAddJ;

public abstract class MemberTypeDecl extends MemberDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MemberTypeDecl clone() throws CloneNotSupportedException {
      MemberTypeDecl node = (MemberTypeDecl)super.clone();
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

   public abstract TypeDecl typeDecl();

   public boolean declaresType(String name) {
      ASTNode$State state = this.state();
      return this.typeDecl().name().equals(name);
   }

   public TypeDecl type(String name) {
      ASTNode$State state = this.state();
      return this.declaresType(name) ? this.typeDecl() : null;
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return this.typeDecl().isStatic();
   }

   public boolean addsIndentationLevel() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return this.typeDecl().hasAnnotationSuppressWarnings(s);
   }

   public boolean isDeprecated() {
      ASTNode$State state = this.state();
      return this.typeDecl().isDeprecated();
   }

   public boolean visibleTypeParameters() {
      ASTNode$State state = this.state();
      return !this.isStatic();
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.typeDecl().hasAnnotationSafeVarargs();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
