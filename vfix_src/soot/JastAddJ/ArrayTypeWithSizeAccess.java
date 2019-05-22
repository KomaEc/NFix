package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayTypeWithSizeAccess extends ArrayTypeAccess implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArrayTypeWithSizeAccess clone() throws CloneNotSupportedException {
      ArrayTypeWithSizeAccess node = (ArrayTypeWithSizeAccess)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayTypeWithSizeAccess copy() {
      try {
         ArrayTypeWithSizeAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayTypeWithSizeAccess fullCopy() {
      ArrayTypeWithSizeAccess tree = this.copy();
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
      this.getAccess().toString(s);
      s.append("[");
      this.getExpr().toString(s);
      s.append("]");
   }

   public void typeCheck() {
      super.typeCheck();
      if (!this.getExpr().type().unaryNumericPromotion().isInt()) {
         this.error(this.getExpr().type().typeName() + " is not int after unary numeric promotion");
      }

   }

   public void addArraySize(Body b, ArrayList list) {
      this.getAccess().addArraySize(b, list);
      list.add(this.asImmediate(b, this.getExpr().eval(b)));
   }

   public ArrayTypeWithSizeAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ArrayTypeWithSizeAccess(Access p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setExpr(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(1);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public void setPackage(String value) {
      this.tokenString_Package = value;
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafter(v);
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? false : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getExprNoTransform() ? this.getAccess().isDAafter(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getExprNoTransform() ? this.getAccess().isDUafter(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      return caller == this.getExprNoTransform() ? this.unqualifiedScope().lookupMethod(name) : this.getParent().Define_Collection_lookupMethod(this, caller, name);
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      return caller == this.getExprNoTransform() ? this.unqualifiedScope().hasPackage(packageName) : this.getParent().Define_boolean_hasPackage(this, caller, packageName);
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      return caller == this.getExprNoTransform() ? this.unqualifiedScope().lookupType(name) : this.getParent().Define_SimpleSet_lookupType(this, caller, name);
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return caller == this.getExprNoTransform() ? this.unqualifiedScope().lookupVariable(name) : this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? NameType.EXPRESSION_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
