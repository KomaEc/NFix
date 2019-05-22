package soot.JastAddJ;

import soot.Value;

public class InstanceOfExpr extends Expr implements Cloneable {
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

   public InstanceOfExpr clone() throws CloneNotSupportedException {
      InstanceOfExpr node = (InstanceOfExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public InstanceOfExpr copy() {
      try {
         InstanceOfExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public InstanceOfExpr fullCopy() {
      InstanceOfExpr tree = this.copy();
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
      this.getExpr().toString(s);
      s.append(" instanceof ");
      this.getTypeAccess().toString(s);
   }

   public void typeCheck() {
      TypeDecl relationalExpr = this.getExpr().type();
      TypeDecl referenceType = this.getTypeAccess().type();
      if (!relationalExpr.isUnknown()) {
         if (!relationalExpr.isReferenceType() && !relationalExpr.isNull()) {
            this.error("The relational expression in instance of must be reference or null type");
         }

         if (!referenceType.isReferenceType()) {
            this.error("The reference expression in instance of must be reference type");
         }

         if (!relationalExpr.castingConversionTo(referenceType)) {
            this.error("The type " + relationalExpr.typeName() + " of the relational expression " + this.getExpr() + " can not be cast into the type " + referenceType.typeName());
         }

         if (this.getExpr().isTypeAccess()) {
            this.error("The relational expression " + this.getExpr() + " must not be a type name");
         }
      }

   }

   public Value eval(Body b) {
      return b.newInstanceOfExpr(this.asImmediate(b, this.getExpr().eval(b)), this.getTypeAccess().type().getSootType(), this);
   }

   public InstanceOfExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public InstanceOfExpr(Expr p0, Access p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExpr(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(0);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v);
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafter(v);
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
      return this.typeBoolean();
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
