package soot.JastAddJ;

import soot.Local;
import soot.Value;

public class BitNotExpr extends Unary implements Cloneable {
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

   public BitNotExpr clone() throws CloneNotSupportedException {
      BitNotExpr node = (BitNotExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BitNotExpr copy() {
      try {
         BitNotExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BitNotExpr fullCopy() {
      BitNotExpr tree = this.copy();
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
      if (!this.getOperand().type().isIntegralType()) {
         this.error("unary ~ only operates on integral types");
      }

   }

   public Value eval(Body b) {
      Value v = IntType.emitConstant(-1);
      Local result = this.asLocal(b, b.newXorExpr(this.asImmediate(b, this.typeInt().emitCastTo(b, v, this.type(), this)), this.asImmediate(b, this.getOperand().eval(b)), this));
      return result;
   }

   public BitNotExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public BitNotExpr(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setOperand(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getOperand() {
      return (Expr)this.getChild(0);
   }

   public Expr getOperandNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.type().bitNot(this.getOperand().constant());
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.getOperand().isConstant();
   }

   public String printPreOp() {
      ASTNode$State state = this.state();
      return "~";
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
      return this.getOperand().type().unaryNumericPromotion();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
