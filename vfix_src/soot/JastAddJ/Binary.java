package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.Value;

public abstract class Binary extends Expr implements Cloneable {
   protected int isConstant_visited = -1;
   protected boolean isConstant_computed = false;
   protected boolean isConstant_initialized = false;
   protected boolean isConstant_value;
   protected Map isDAafterTrue_Variable_values;
   protected Map isDAafterFalse_Variable_values;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map isDUbefore_Variable_values;

   public void flushCache() {
      super.flushCache();
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
      this.isDAafterTrue_Variable_values = null;
      this.isDAafterFalse_Variable_values = null;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.isDUbefore_Variable_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Binary clone() throws CloneNotSupportedException {
      Binary node = (Binary)super.clone();
      node.isConstant_visited = -1;
      node.isConstant_computed = false;
      node.isConstant_initialized = false;
      node.isDAafterTrue_Variable_values = null;
      node.isDAafterFalse_Variable_values = null;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.isDUbefore_Variable_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void toString(StringBuffer s) {
      this.getLeftOperand().toString(s);
      s.append(this.printOp());
      this.getRightOperand().toString(s);
   }

   public Value eval(Body b) {
      return this.asLocal(b, this.emitOperation(b, this.getLeftOperand().type().emitCastTo(b, this.getLeftOperand(), this.type()), this.getRightOperand().type().emitCastTo(b, this.getRightOperand(), this.type())));
   }

   public Value emitShiftExpr(Body b) {
      return this.asLocal(b, this.emitOperation(b, this.getLeftOperand().type().emitCastTo(b, this.getLeftOperand(), this.type()), this.getRightOperand().type().emitCastTo(b, this.getRightOperand(), this.typeInt())));
   }

   public Value emitOperation(Body b, Value left, Value right) {
      throw new Error("emitOperation not implemented in " + this.getClass().getName());
   }

   public Binary() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public Binary(Expr p0, Expr p1) {
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

   private TypeDecl refined_ConstantExpression_Binary_binaryNumericPromotedType() {
      TypeDecl leftType = this.left().type();
      TypeDecl rightType = this.right().type();
      if (leftType.isString()) {
         return leftType;
      } else if (rightType.isString()) {
         return rightType;
      } else if (leftType.isNumericType() && rightType.isNumericType()) {
         return leftType.binaryNumericPromotion(rightType);
      } else {
         return leftType.isBoolean() && rightType.isBoolean() ? leftType : this.unknownType();
      }
   }

   public abstract String printOp();

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
      return this.getLeftOperand().isConstant() && this.getRightOperand().isConstant();
   }

   public Expr left() {
      ASTNode$State state = this.state();
      return this.getLeftOperand();
   }

   public Expr right() {
      ASTNode$State state = this.state();
      return this.getRightOperand();
   }

   public TypeDecl binaryNumericPromotedType() {
      ASTNode$State state = this.state();
      TypeDecl leftType = this.left().type();
      TypeDecl rightType = this.right().type();
      if (leftType.isBoolean() && rightType.isBoolean()) {
         return leftType.isReferenceType() ? leftType.unboxed() : leftType;
      } else {
         return this.refined_ConstantExpression_Binary_binaryNumericPromotedType();
      }
   }

   public boolean isDAafterTrue(Variable v) {
      if (this.isDAafterTrue_Variable_values == null) {
         this.isDAafterTrue_Variable_values = new HashMap(4);
      }

      if (this.isDAafterTrue_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterTrue_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterTrue_Variable_value = this.isDAafterTrue_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterTrue_Variable_values.put(v, isDAafterTrue_Variable_value);
         }

         return isDAafterTrue_Variable_value;
      }
   }

   private boolean isDAafterTrue_compute(Variable v) {
      return this.getRightOperand().isDAafter(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      if (this.isDAafterFalse_Variable_values == null) {
         this.isDAafterFalse_Variable_values = new HashMap(4);
      }

      if (this.isDAafterFalse_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterFalse_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterFalse_Variable_value = this.isDAafterFalse_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterFalse_Variable_values.put(v, isDAafterFalse_Variable_value);
         }

         return isDAafterFalse_Variable_value;
      }
   }

   private boolean isDAafterFalse_compute(Variable v) {
      return this.getRightOperand().isDAafter(v) || this.isTrue();
   }

   public boolean isDAafter(Variable v) {
      if (this.isDAafter_Variable_values == null) {
         this.isDAafter_Variable_values = new HashMap(4);
      }

      if (this.isDAafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafter_Variable_value = this.isDAafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafter_Variable_values.put(v, isDAafter_Variable_value);
         }

         return isDAafter_Variable_value;
      }
   }

   private boolean isDAafter_compute(Variable v) {
      return this.getRightOperand().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      if (this.isDUafter_Variable_values == null) {
         this.isDUafter_Variable_values = new HashMap(4);
      }

      if (this.isDUafter_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUafter_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUafter_Variable_value = this.isDUafter_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUafter_Variable_values.put(v, isDUafter_Variable_value);
         }

         return isDUafter_Variable_value;
      }
   }

   private boolean isDUafter_compute(Variable v) {
      return this.getRightOperand().isDUafter(v);
   }

   public boolean isDUbefore(Variable v) {
      if (this.isDUbefore_Variable_values == null) {
         this.isDUbefore_Variable_values = new HashMap(4);
      }

      if (this.isDUbefore_Variable_values.containsKey(v)) {
         return (Boolean)this.isDUbefore_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDUbefore_Variable_value = this.getParent().Define_boolean_isDUbefore(this, (ASTNode)null, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDUbefore_Variable_values.put(v, isDUbefore_Variable_value);
         }

         return isDUbefore_Variable_value;
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getRightOperandNoTransform() ? this.getLeftOperand().isDAafter(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getRightOperandNoTransform() ? this.getLeftOperand().isDUafter(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
