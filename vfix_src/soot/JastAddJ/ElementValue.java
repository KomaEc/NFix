package soot.JastAddJ;

import java.util.Collection;

public abstract class ElementValue extends ASTNode<ASTNode> implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ElementValue clone() throws CloneNotSupportedException {
      ElementValue node = (ElementValue)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void appendAsAttributeTo(Collection list, String name) {
      throw new Error(this.getClass().getName() + " does not support appendAsAttributeTo(Attribute buf)");
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public boolean validTarget(Annotation a) {
      ASTNode$State state = this.state();
      return false;
   }

   public ElementValue definesElementTypeValue(String name) {
      ASTNode$State state = this.state();
      return null;
   }

   public boolean hasValue(String s) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean commensurateWithTypeDecl(TypeDecl type) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean commensurateWithArrayDecl(ArrayDecl type) {
      ASTNode$State state = this.state();
      return type.componentType().commensurateWith(this);
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.unknownType();
   }

   public TypeDecl enclosingAnnotationDecl() {
      ASTNode$State state = this.state();
      TypeDecl enclosingAnnotationDecl_value = this.getParent().Define_TypeDecl_enclosingAnnotationDecl(this, (ASTNode)null);
      return enclosingAnnotationDecl_value;
   }

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
