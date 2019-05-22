package soot.JastAddJ;

import soot.Value;

public class ModExpr extends MultiplicativeExpr implements Cloneable {
   protected int isConstant_visited = -1;
   protected boolean isConstant_computed = false;
   protected boolean isConstant_initialized = false;
   protected boolean isConstant_value;

   public void flushCache() {
      super.flushCache();
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ModExpr clone() throws CloneNotSupportedException {
      ModExpr node = (ModExpr)super.clone();
      node.isConstant_visited = -1;
      node.isConstant_computed = false;
      node.isConstant_initialized = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ModExpr copy() {
      try {
         ModExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ModExpr fullCopy() {
      ModExpr tree = this.copy();
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

   public Value emitOperation(Body b, Value left, Value right) {
      return this.asLocal(b, b.newRemExpr(this.asImmediate(b, left), this.asImmediate(b, right), this));
   }

   public ModExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ModExpr(Expr p0, Expr p1) {
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
      return this.type().mod(this.getLeftOperand().constant(), this.getRightOperand().constant());
   }

   public boolean isConstant() {
      if (this.isConstant_computed) {
         return this.isConstant_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.isConstant_initialized) {
            this.isConstant_initialized = true;
            this.isConstant_value = false;
         }

         if (state.IN_CIRCLE) {
            if (this.isConstant_visited != state.CIRCLE_INDEX) {
               this.isConstant_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.isConstant_computed = false;
                  this.isConstant_initialized = false;
                  this.isConstant_visited = -1;
                  return this.isConstant_value;
               } else {
                  boolean new_isConstant_value = this.isConstant_compute();
                  if (new_isConstant_value != this.isConstant_value) {
                     state.CHANGE = true;
                  }

                  this.isConstant_value = new_isConstant_value;
                  return this.isConstant_value;
               }
            } else {
               return this.isConstant_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.isConstant_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_isConstant_value = this.isConstant_compute();
               if (new_isConstant_value != this.isConstant_value) {
                  state.CHANGE = true;
               }

               this.isConstant_value = new_isConstant_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.isConstant_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.isConstant_compute();
               state.RESET_CYCLE = false;
               this.isConstant_computed = false;
               this.isConstant_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.isConstant_value;
         }
      }
   }

   private boolean isConstant_compute() {
      return this.getLeftOperand().isConstant() && this.getRightOperand().isConstant() && (!this.getRightOperand().type().isInt() || this.getRightOperand().constant().intValue() != 0);
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " % ";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
