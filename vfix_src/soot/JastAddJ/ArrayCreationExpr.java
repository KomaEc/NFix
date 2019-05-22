package soot.JastAddJ;

import java.util.ArrayList;
import soot.ArrayType;
import soot.Value;

public class ArrayCreationExpr extends PrimaryExpr implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected boolean numArrays_computed = false;
   protected int numArrays_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
      this.numArrays_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArrayCreationExpr clone() throws CloneNotSupportedException {
      ArrayCreationExpr node = (ArrayCreationExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.numArrays_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayCreationExpr copy() {
      try {
         ArrayCreationExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayCreationExpr fullCopy() {
      ArrayCreationExpr tree = this.copy();
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
      s.append("new ");
      this.getTypeAccess().toString(s);
      if (this.hasArrayInit()) {
         this.getArrayInit().toString(s);
      }

   }

   public Value eval(Body b) {
      if (this.hasArrayInit()) {
         return this.getArrayInit().eval(b);
      } else {
         ArrayList list = new ArrayList();
         this.getTypeAccess().addArraySize(b, list);
         if (this.numArrays() == 1) {
            Value size = (Value)list.get(0);
            return b.newNewArrayExpr(this.type().componentType().getSootType(), this.asImmediate(b, size), this);
         } else {
            return b.newNewMultiArrayExpr((ArrayType)this.type().getSootType(), list, this);
         }
      }
   }

   public ArrayCreationExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new Opt(), 1);
   }

   public ArrayCreationExpr(Access p0, Opt<ArrayInit> p1) {
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

   public void setArrayInitOpt(Opt<ArrayInit> opt) {
      this.setChild(opt, 1);
   }

   public boolean hasArrayInit() {
      return this.getArrayInitOpt().getNumChild() != 0;
   }

   public ArrayInit getArrayInit() {
      return (ArrayInit)this.getArrayInitOpt().getChild(0);
   }

   public void setArrayInit(ArrayInit node) {
      this.getArrayInitOpt().setChild(node, 0);
   }

   public Opt<ArrayInit> getArrayInitOpt() {
      return (Opt)this.getChild(1);
   }

   public Opt<ArrayInit> getArrayInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   public boolean isDAafterCreation(Variable v) {
      ASTNode$State state = this.state();
      return this.getTypeAccess().isDAafter(v);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.hasArrayInit() ? this.getArrayInit().isDAafter(v) : this.isDAafterCreation(v);
   }

   public boolean isDUafterCreation(Variable v) {
      ASTNode$State state = this.state();
      return this.getTypeAccess().isDUafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.hasArrayInit() ? this.getArrayInit().isDUafter(v) : this.isDUafterCreation(v);
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

   public int numArrays() {
      if (this.numArrays_computed) {
         return this.numArrays_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.numArrays_value = this.numArrays_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.numArrays_computed = true;
         }

         return this.numArrays_value;
      }
   }

   private int numArrays_compute() {
      int i = this.type().dimension();

      for(Access a = this.getTypeAccess(); a instanceof ArrayTypeAccess && !(a instanceof ArrayTypeWithSizeAccess); a = ((ArrayTypeAccess)a).getAccess()) {
         --i;
      }

      return i;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getArrayInitOptNoTransform() ? this.isDAafterCreation(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getArrayInitOptNoTransform() ? this.isDUafterCreation(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      return caller == this.getArrayInitOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_declType(this, caller);
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      return caller == this.getArrayInitOptNoTransform() ? this.type().componentType() : this.getParent().Define_TypeDecl_expectedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
