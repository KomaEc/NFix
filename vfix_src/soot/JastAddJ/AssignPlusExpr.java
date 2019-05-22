package soot.JastAddJ;

import soot.Local;
import soot.Scene;
import soot.Value;

public class AssignPlusExpr extends AssignAdditiveExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AssignPlusExpr clone() throws CloneNotSupportedException {
      AssignPlusExpr node = (AssignPlusExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AssignPlusExpr copy() {
      try {
         AssignPlusExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AssignPlusExpr fullCopy() {
      AssignPlusExpr tree = this.copy();
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
      } else {
         if (this.getSource().type().isUnknown() || this.getDest().type().isUnknown()) {
            return;
         }

         if (this.getDest().type().isString() && !this.getSource().type().isVoid()) {
            return;
         }

         if (!this.getSource().type().isBoolean() && !this.getDest().type().isBoolean()) {
            if (this.getSource().type().isPrimitive() && this.getDest().type().isPrimitive()) {
               return;
            }

            this.error("can not assign " + this.getDest() + " of type " + this.getDest().type().typeName() + " a value of type " + this.sourceType().typeName());
         } else {
            this.error("Operator + does not operate on boolean types");
         }
      }

   }

   public Value eval(Body b) {
      TypeDecl dest = this.getDest().type();
      TypeDecl source = this.getSource().type();
      if (dest.isString()) {
         Value lvalue = this.getDest().eval(b);
         Value v = this.asImmediate(b, lvalue);
         Local local = b.newTemp((Value)b.newNewExpr(this.lookupType("java.lang", "StringBuffer").sootRef(), this));
         b.setLine(this);
         b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, Scene.v().getMethod("<java.lang.StringBuffer: void <init>(java.lang.String)>").makeRef(), (Value)v, this), this));
         Local rightResult = b.newTemp((Value)b.newVirtualInvokeExpr(local, this.lookupType("java.lang", "StringBuffer").methodWithArgs("append", new TypeDecl[]{source.stringPromotion()}).sootRef(), (Value)this.asImmediate(b, this.getSource().eval(b)), this));
         Local result = b.newTemp((Value)b.newVirtualInvokeExpr(rightResult, Scene.v().getMethod("<java.lang.StringBuffer: java.lang.String toString()>").makeRef(), this));
         Value v2 = lvalue instanceof Local ? lvalue : (Value)lvalue.clone();
         this.getDest().emitStore(b, v2, result, this);
         return result;
      } else {
         return super.eval(b);
      }
   }

   public Value createAssignOp(Body b, Value fst, Value snd) {
      return b.newAddExpr(this.asImmediate(b, fst), this.asImmediate(b, snd), this);
   }

   public AssignPlusExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignPlusExpr(Expr p0, Expr p1) {
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
      return " += ";
   }

   public TypeDecl sourceType() {
      ASTNode$State state = this.state();
      TypeDecl left = this.getDest().type();
      TypeDecl right = this.getSource().type();
      if (!left.isString() && !right.isString()) {
         return super.sourceType();
      } else if (!left.isVoid() && !right.isVoid()) {
         return left.isString() ? left : right;
      } else {
         return this.unknownType();
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
