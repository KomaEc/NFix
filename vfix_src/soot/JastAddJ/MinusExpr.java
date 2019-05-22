package soot.JastAddJ;

import soot.Value;

public class MinusExpr extends Unary implements Cloneable {
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

   public MinusExpr clone() throws CloneNotSupportedException {
      MinusExpr node = (MinusExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MinusExpr copy() {
      try {
         MinusExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MinusExpr fullCopy() {
      MinusExpr tree = this.copy();
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
      if (!this.getOperand().type().isNumericType()) {
         this.error("unary minus only operates on numeric types");
      }

   }

   public Value eval(Body b) {
      return b.newNegExpr(this.asImmediate(b, this.getOperand().eval(b)), this);
   }

   public MinusExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public MinusExpr(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return true;
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
      return this.type().minus(this.getOperand().constant());
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.getOperand().isConstant();
   }

   public String printPreOp() {
      ASTNode$State state = this.state();
      return "-";
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
      if (this.getOperand() instanceof IntegerLiteral && ((IntegerLiteral)this.getOperand()).isDecimal() && this.getOperand().isPositive()) {
         ++this.state().duringLiterals;
         ASTNode result = this.rewriteRule0();
         --this.state().duringLiterals;
         return result;
      } else if (this.getOperand() instanceof LongLiteral && ((LongLiteral)this.getOperand()).isDecimal() && this.getOperand().isPositive()) {
         ++this.state().duringLiterals;
         ASTNode result = this.rewriteRule1();
         --this.state().duringLiterals;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private IntegerLiteral rewriteRule0() {
      IntegerLiteral original = (IntegerLiteral)this.getOperand();
      IntegerLiteral literal = new IntegerLiteral("-" + original.getLITERAL());
      literal.setDigits(original.getDigits());
      literal.setKind(original.getKind());
      return literal;
   }

   private LongLiteral rewriteRule1() {
      LongLiteral original = (LongLiteral)this.getOperand();
      LongLiteral literal = new LongLiteral("-" + original.getLITERAL());
      literal.setDigits(original.getDigits());
      literal.setKind(original.getKind());
      return literal;
   }
}
