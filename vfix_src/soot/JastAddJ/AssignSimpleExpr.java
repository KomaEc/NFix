package soot.JastAddJ;

import soot.Value;

public class AssignSimpleExpr extends AssignExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssignSimpleExpr clone() throws CloneNotSupportedException {
      AssignSimpleExpr node = (AssignSimpleExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AssignSimpleExpr copy() {
      try {
         AssignSimpleExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AssignSimpleExpr fullCopy() {
      AssignSimpleExpr tree = this.copy();
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

   public void typeCheck() {
      if (!this.getDest().isVariable()) {
         this.error("left hand side is not a variable");
      } else if (!this.sourceType().assignConversionTo(this.getDest().type(), this.getSource()) && !this.sourceType().isUnknown()) {
         this.error("can not assign " + this.getDest() + " of type " + this.getDest().type().typeName() + " a value of type " + this.sourceType().typeName());
      }

   }

   public Value eval(Body b) {
      Value lvalue = this.getDest().eval(b);
      Value rvalue = this.asRValue(b, this.getSource().type().emitCastTo(b, this.getSource(), this.getDest().type()));
      return this.getDest().emitStore(b, lvalue, this.asImmediate(b, rvalue), this);
   }

   public void checkWarnings() {
      if (!this.withinSuppressWarnings("unchecked")) {
         this.checkUncheckedConversion(this.getSource().type(), this.getDest().type());
      }

   }

   public AssignSimpleExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignSimpleExpr(Expr p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setDest(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getDest() {
      return (Expr)this.getChild(0);
   }

   public Expr getDestNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setSource(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getSource() {
      return (Expr)this.getChild(1);
   }

   public Expr getSourceNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " = ";
   }

   public TypeDecl sourceType() {
      ASTNode$State state = this.state();
      return this.getSource().type();
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getDestNoTransform() ? true : super.Define_boolean_isDest(caller, child);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getDestNoTransform() ? false : super.Define_boolean_isSource(caller, child);
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      return caller == this.getSourceNoTransform() ? this.getDest().type() : this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
