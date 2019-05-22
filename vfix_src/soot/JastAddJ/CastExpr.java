package soot.JastAddJ;

import soot.Value;
import soot.jimple.NullConstant;

public class CastExpr extends Expr implements Cloneable {
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

   public CastExpr clone() throws CloneNotSupportedException {
      CastExpr node = (CastExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public CastExpr copy() {
      try {
         CastExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public CastExpr fullCopy() {
      CastExpr tree = this.copy();
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
      s.append("(");
      this.getTypeAccess().toString(s);
      s.append(")");
      this.getExpr().toString(s);
   }

   public void typeCheck() {
      TypeDecl expr = this.getExpr().type();
      TypeDecl type = this.getTypeAccess().type();
      if (!expr.isUnknown()) {
         if (!expr.castingConversionTo(type)) {
            this.error(expr.typeName() + " can not be cast into " + type.typeName());
         }

         if (!this.getTypeAccess().isTypeAccess()) {
            this.error("" + this.getTypeAccess() + " is not a type access in cast expression");
         }
      }

   }

   public Value eval(Body b) {
      if (this.isConstant()) {
         return emitConstant(this.constant());
      } else {
         Value operand = this.getExpr().eval(b);
         return operand == NullConstant.v() ? this.getExpr().type().emitCastTo(b, operand, this.type(), this) : this.getExpr().type().emitCastTo(b, this.asLocal(b, operand), this.type(), this);
      }
   }

   public void checkWarnings() {
      if (!this.withinSuppressWarnings("unchecked")) {
         this.checkUncheckedConversion(this.getExpr().type(), this.getTypeAccess().type());
      }

   }

   public CastExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public CastExpr(Access p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(0);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
   }

   public void setExpr(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(1);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.type().cast(this.getExpr().constant());
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.getExpr().isConstant() && (this.getTypeAccess().type().isPrimitive() || this.getTypeAccess().type().isString());
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafter(v);
   }

   public boolean isSuperAccess() {
      ASTNode$State state = this.state();
      return this.getExpr().isSuperAccess();
   }

   public boolean isThisAccess() {
      ASTNode$State state = this.state();
      return this.getExpr().isThisAccess();
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
      return this.getTypeAccess().type();
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return this.getExpr().staticContextQualifier();
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
