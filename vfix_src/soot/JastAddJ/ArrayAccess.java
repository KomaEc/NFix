package soot.JastAddJ;

import java.util.Collection;
import soot.Value;

public class ArrayAccess extends Access implements Cloneable {
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

   public ArrayAccess clone() throws CloneNotSupportedException {
      ArrayAccess node = (ArrayAccess)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayAccess copy() {
      try {
         ArrayAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayAccess fullCopy() {
      ArrayAccess tree = this.copy();
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
      s.append("[");
      this.getExpr().toString(s);
      s.append("]");
   }

   public void typeCheck() {
      if (this.isQualified() && !this.qualifier().type().isArrayDecl() && !this.qualifier().type().isUnknown()) {
         this.error("the type " + this.qualifier().type().name() + " of the indexed element is not an array");
      }

      if (!this.getExpr().type().unaryNumericPromotion().isInt() || !this.getExpr().type().isIntegralType()) {
         this.error("array index must be int after unary numeric promotion which " + this.getExpr().type().typeName() + " is not");
      }

   }

   public Value eval(Body b) {
      Value arrayRef = b.newTemp(this.prevExpr().eval(b));
      Value arrayIndex = b.newTemp(this.getExpr().eval(b));
      return b.newArrayRef(this.asLocal(b, arrayRef), this.asImmediate(b, arrayIndex), this);
   }

   public ArrayAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ArrayAccess(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
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

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getExpr().isDUafter(v);
   }

   public boolean isArrayAccess() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.EXPRESSION_NAME;
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
      return this.isQualified() ? this.qualifier().type().componentType() : this.unknownType();
   }

   public boolean isVariable() {
      ASTNode$State state = this.state();
      return true;
   }

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? false : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getExprNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
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
