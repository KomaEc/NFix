package soot.JastAddJ;

import soot.Local;
import soot.Scene;
import soot.Value;
import soot.jimple.StringConstant;

public class AddExpr extends AdditiveExpr implements Cloneable {
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

   public AddExpr clone() throws CloneNotSupportedException {
      AddExpr node = (AddExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AddExpr copy() {
      try {
         AddExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AddExpr fullCopy() {
      AddExpr tree = this.copy();
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
      TypeDecl left = this.getLeftOperand().type();
      TypeDecl right = this.getRightOperand().type();
      if (!left.isString() && !right.isString()) {
         super.typeCheck();
      } else if (left.isVoid()) {
         this.error("The type void of the left hand side is not numeric");
      } else if (right.isVoid()) {
         this.error("The type void of the right hand side is not numeric");
      }

   }

   public Value emitOperation(Body b, Value left, Value right) {
      return this.asLocal(b, b.newAddExpr(this.asImmediate(b, left), this.asImmediate(b, right), this));
   }

   public Value eval(Body b) {
      if (this.type().isString() && this.isConstant()) {
         return StringConstant.v(this.constant().stringValue());
      } else if (this.isStringAdd()) {
         Local v;
         if (this.firstStringAddPart()) {
            v = b.newTemp((Value)b.newNewExpr(this.lookupType("java.lang", "StringBuffer").sootRef(), this));
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(v, Scene.v().getMethod("<java.lang.StringBuffer: void <init>()>").makeRef(), this), this));
            b.setLine(this);
            b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(v, this.lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{this.getLeftOperand().type().stringPromotion()}).sootRef(), (Value)this.asImmediate(b, this.getLeftOperand().eval(b)), this), this));
         } else {
            v = (Local)this.getLeftOperand().eval(b);
         }

         b.setLine(this);
         b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(v, this.lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{this.getRightOperand().type().stringPromotion()}).sootRef(), (Value)this.asImmediate(b, this.getRightOperand().eval(b)), this), this));
         return this.lastStringAddPart() ? b.newTemp((Value)b.newVirtualInvokeExpr(v, Scene.v().getMethod("<java.lang.StringBuffer: java.lang.String toString()>").makeRef(), this)) : v;
      } else {
         return b.newAddExpr(b.newTemp(this.getLeftOperand().type().emitCastTo(b, this.getLeftOperand(), this.type())), this.asImmediate(b, this.getRightOperand().type().emitCastTo(b, this.getRightOperand(), this.type())), this);
      }
   }

   public AddExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AddExpr(Expr p0, Expr p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLeftOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getLeftOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getLeftOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setRightOperand(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getRightOperand() {
      return (Expr)this.getChild(1);
   }

   public Expr getRightOperandNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.type().add(this.getLeftOperand().constant(), this.getRightOperand().constant());
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " + ";
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
      TypeDecl left = this.getLeftOperand().type();
      TypeDecl right = this.getRightOperand().type();
      if (!left.isString() && !right.isString()) {
         return super.type();
      } else if (!left.isVoid() && !right.isVoid()) {
         return left.isString() ? left : right;
      } else {
         return this.unknownType();
      }
   }

   public boolean isStringAdd() {
      ASTNode$State state = this.state();
      return this.type().isString() && !this.isConstant();
   }

   public boolean firstStringAddPart() {
      ASTNode$State state = this.state();
      return this.type().isString() && !this.getLeftOperand().isStringAdd();
   }

   public boolean lastStringAddPart() {
      ASTNode$State state = this.state();
      return !this.getParent().isStringAdd();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
