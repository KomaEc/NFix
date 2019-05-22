package soot.JastAddJ;

import java.util.ArrayList;
import soot.Local;
import soot.Value;

public class ConditionalExpr extends Expr implements Cloneable {
   protected boolean constant_computed = false;
   protected Constant constant_value;
   protected boolean isConstant_computed = false;
   protected boolean isConstant_value;
   protected boolean booleanOperator_computed = false;
   protected boolean booleanOperator_value;
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected boolean else_branch_label_computed = false;
   protected soot.jimple.Stmt else_branch_label_value;
   protected boolean then_branch_label_computed = false;
   protected soot.jimple.Stmt then_branch_label_value;

   public void flushCache() {
      super.flushCache();
      this.constant_computed = false;
      this.constant_value = null;
      this.isConstant_computed = false;
      this.booleanOperator_computed = false;
      this.type_computed = false;
      this.type_value = null;
      this.else_branch_label_computed = false;
      this.else_branch_label_value = null;
      this.then_branch_label_computed = false;
      this.then_branch_label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ConditionalExpr clone() throws CloneNotSupportedException {
      ConditionalExpr node = (ConditionalExpr)super.clone();
      node.constant_computed = false;
      node.constant_value = null;
      node.isConstant_computed = false;
      node.booleanOperator_computed = false;
      node.type_computed = false;
      node.type_value = null;
      node.else_branch_label_computed = false;
      node.else_branch_label_value = null;
      node.then_branch_label_computed = false;
      node.then_branch_label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ConditionalExpr copy() {
      try {
         ConditionalExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ConditionalExpr fullCopy() {
      ConditionalExpr tree = this.copy();
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
      this.getCondition().toString(s);
      s.append(" ? ");
      this.getTrueExpr().toString(s);
      s.append(" : ");
      this.getFalseExpr().toString(s);
   }

   public void typeCheck() {
      if (!this.getCondition().type().isBoolean()) {
         this.error("The first operand of a conditional expression must be a boolean");
      }

      if (this.type().isUnknown() && !this.getTrueExpr().type().isUnknown() && !this.getFalseExpr().type().isUnknown()) {
         this.error("The types of the second and third operand in this conditional expression do not match");
      }

   }

   public Value eval(Body b) {
      b.setLine(this);
      if (this.type().isBoolean()) {
         return this.emitBooleanCondition(b);
      } else {
         Local result = b.newTemp(this.type().getSootType());
         soot.jimple.Stmt endBranch = this.newLabel();
         this.getCondition().emitEvalBranch(b);
         if (this.getCondition().canBeTrue()) {
            b.addLabel(this.then_branch_label());
            b.add(b.newAssignStmt(result, this.getTrueExpr().type().emitCastTo(b, this.getTrueExpr(), this.type()), this));
            if (this.getCondition().canBeFalse()) {
               b.add(b.newGotoStmt(endBranch, this));
            }
         }

         if (this.getCondition().canBeFalse()) {
            b.addLabel(this.else_branch_label());
            b.add(b.newAssignStmt(result, this.getFalseExpr().type().emitCastTo(b, this.getFalseExpr(), this.type()), this));
         }

         b.addLabel(endBranch);
         return result;
      }
   }

   public void emitEvalBranch(Body b) {
      b.setLine(this);
      soot.jimple.Stmt endBranch = this.newLabel();
      this.getCondition().emitEvalBranch(b);
      b.addLabel(this.then_branch_label());
      if (this.getCondition().canBeTrue()) {
         this.getTrueExpr().emitEvalBranch(b);
         b.add(b.newGotoStmt(this.true_label(), this));
      }

      b.addLabel(this.else_branch_label());
      if (this.getCondition().canBeFalse()) {
         this.getFalseExpr().emitEvalBranch(b);
         b.add(b.newGotoStmt(this.true_label(), this));
      }

   }

   public ConditionalExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
   }

   public ConditionalExpr(Expr p0, Expr p1, Expr p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setCondition(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getCondition() {
      return (Expr)this.getChild(0);
   }

   public Expr getConditionNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setTrueExpr(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getTrueExpr() {
      return (Expr)this.getChild(1);
   }

   public Expr getTrueExprNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public void setFalseExpr(Expr node) {
      this.setChild(node, 2);
   }

   public Expr getFalseExpr() {
      return (Expr)this.getChild(2);
   }

   public Expr getFalseExprNoTransform() {
      return (Expr)this.getChildNoTransform(2);
   }

   private TypeDecl refined_TypeAnalysis_ConditionalExpr_type() {
      TypeDecl trueType = this.getTrueExpr().type();
      TypeDecl falseType = this.getFalseExpr().type();
      if (trueType == falseType) {
         return trueType;
      } else if (trueType.isNumericType() && falseType.isNumericType()) {
         if (trueType.isByte() && falseType.isShort()) {
            return falseType;
         } else if (trueType.isShort() && falseType.isByte()) {
            return trueType;
         } else if ((trueType.isByte() || trueType.isShort() || trueType.isChar()) && falseType.isInt() && this.getFalseExpr().isConstant() && this.getFalseExpr().representableIn(trueType)) {
            return trueType;
         } else {
            return (falseType.isByte() || falseType.isShort() || falseType.isChar()) && trueType.isInt() && this.getTrueExpr().isConstant() && this.getTrueExpr().representableIn(falseType) ? falseType : trueType.binaryNumericPromotion(falseType);
         }
      } else if (trueType.isBoolean() && falseType.isBoolean()) {
         return trueType;
      } else if (trueType.isReferenceType() && falseType.isNull()) {
         return trueType;
      } else if (trueType.isNull() && falseType.isReferenceType()) {
         return falseType;
      } else if (trueType.isReferenceType() && falseType.isReferenceType()) {
         if (trueType.assignConversionTo(falseType, (Expr)null)) {
            return falseType;
         } else {
            return falseType.assignConversionTo(trueType, (Expr)null) ? trueType : this.unknownType();
         }
      } else {
         return this.unknownType();
      }
   }

   private TypeDecl refined_AutoBoxing_ConditionalExpr_type() {
      TypeDecl trueType = this.getTrueExpr().type();
      TypeDecl falseType = this.getFalseExpr().type();
      if (trueType.isBoolean() && falseType.isBoolean()) {
         if (trueType == falseType) {
            return trueType;
         } else {
            return trueType.isReferenceType() ? trueType.unboxed() : trueType;
         }
      } else {
         return this.refined_TypeAnalysis_ConditionalExpr_type();
      }
   }

   public Constant constant() {
      if (this.constant_computed) {
         return this.constant_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.constant_value = this.constant_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.constant_computed = true;
         }

         return this.constant_value;
      }
   }

   private Constant constant_compute() {
      return this.type().questionColon(this.getCondition().constant(), this.getTrueExpr().constant(), this.getFalseExpr().constant());
   }

   public boolean isConstant() {
      if (this.isConstant_computed) {
         return this.isConstant_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isConstant_value = this.isConstant_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isConstant_computed = true;
         }

         return this.isConstant_value;
      }
   }

   private boolean isConstant_compute() {
      return this.getCondition().isConstant() && this.getTrueExpr().isConstant() && this.getFalseExpr().isConstant();
   }

   public boolean booleanOperator() {
      if (this.booleanOperator_computed) {
         return this.booleanOperator_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.booleanOperator_value = this.booleanOperator_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.booleanOperator_computed = true;
         }

         return this.booleanOperator_value;
      }
   }

   private boolean booleanOperator_compute() {
      return this.getTrueExpr().type().isBoolean() && this.getFalseExpr().type().isBoolean();
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getTrueExpr().isDAafterTrue(v) && this.getFalseExpr().isDAafterTrue(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getTrueExpr().isDAafterFalse(v) && this.getFalseExpr().isDAafterFalse(v) || this.isTrue();
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.booleanOperator() ? this.isDAafterTrue(v) && this.isDAafterFalse(v) : this.getTrueExpr().isDAafter(v) && this.getFalseExpr().isDAafter(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.getTrueExpr().isDUafterTrue(v) && this.getFalseExpr().isDUafterTrue(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.getTrueExpr().isDUafterFalse(v) && this.getFalseExpr().isDUafterFalse(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.booleanOperator() ? this.isDUafterTrue(v) && this.isDUafterFalse(v) : this.getTrueExpr().isDUafter(v) && this.getFalseExpr().isDUafter(v);
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
      TypeDecl type = this.refined_AutoBoxing_ConditionalExpr_type();
      TypeDecl trueType = this.getTrueExpr().type();
      TypeDecl falseType = this.getFalseExpr().type();
      if (type.isUnknown()) {
         if (!trueType.isReferenceType() && !trueType.boxed().isUnknown()) {
            trueType = trueType.boxed();
         }

         if (!falseType.isReferenceType() && !falseType.boxed().isUnknown()) {
            falseType = falseType.boxed();
         }

         ArrayList list = new ArrayList();
         list.add(trueType);
         list.add(falseType);
         return type.lookupLUBType(list);
      } else {
         return type;
      }
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return this.type().isBoolean() && (this.getTrueExpr().canBeTrue() && this.getFalseExpr().canBeTrue() || this.getCondition().isTrue() && this.getTrueExpr().canBeTrue() || this.getCondition().isFalse() && this.getFalseExpr().canBeTrue());
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return this.type().isBoolean() && (this.getTrueExpr().canBeFalse() && this.getFalseExpr().canBeFalse() || this.getCondition().isTrue() && this.getTrueExpr().canBeFalse() || this.getCondition().isFalse() && this.getFalseExpr().canBeFalse());
   }

   public soot.jimple.Stmt else_branch_label() {
      if (this.else_branch_label_computed) {
         return this.else_branch_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.else_branch_label_value = this.else_branch_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.else_branch_label_computed = true;
         }

         return this.else_branch_label_value;
      }
   }

   private soot.jimple.Stmt else_branch_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt then_branch_label() {
      if (this.then_branch_label_computed) {
         return this.then_branch_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.then_branch_label_value = this.then_branch_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.then_branch_label_computed = true;
         }

         return this.then_branch_label_value;
      }
   }

   private soot.jimple.Stmt then_branch_label_compute() {
      return this.newLabel();
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getFalseExprNoTransform()) {
         return this.getCondition().isDAafterFalse(v);
      } else if (caller == this.getTrueExprNoTransform()) {
         return this.getCondition().isDAafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getFalseExprNoTransform()) {
         return this.getCondition().isDUafterFalse(v);
      } else if (caller == this.getTrueExprNoTransform()) {
         return this.getCondition().isDUafterTrue(v);
      } else {
         return caller == this.getConditionNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
      if (caller == this.getFalseExprNoTransform()) {
         return this.false_label();
      } else if (caller == this.getTrueExprNoTransform()) {
         return this.false_label();
      } else {
         return caller == this.getConditionNoTransform() ? this.else_branch_label() : this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
      }
   }

   public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
      if (caller == this.getFalseExprNoTransform()) {
         return this.true_label();
      } else if (caller == this.getTrueExprNoTransform()) {
         return this.true_label();
      } else {
         return caller == this.getConditionNoTransform() ? this.then_branch_label() : this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
