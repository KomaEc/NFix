package soot.JastAddJ;

import soot.Local;
import soot.Value;

public abstract class AssignExpr extends Expr implements Cloneable {
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

   public AssignExpr clone() throws CloneNotSupportedException {
      AssignExpr node = (AssignExpr)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   protected boolean checkDUeverywhere(Variable v) {
      return this.getDest().isVariable() && this.getDest().varDecl() == v && !this.getSource().isDAafter(v) ? false : super.checkDUeverywhere(v);
   }

   public static Stmt asStmt(Expr left, Expr right) {
      return new ExprStmt(new AssignSimpleExpr(left, right));
   }

   public void toString(StringBuffer s) {
      this.getDest().toString(s);
      s.append(this.printOp());
      this.getSource().toString(s);
   }

   public void typeCheck() {
      if (!this.getDest().isVariable()) {
         this.error("left hand side is not a variable");
      } else {
         TypeDecl source = this.sourceType();
         TypeDecl dest = this.getDest().type();
         if (this.getSource().type().isPrimitive() && this.getDest().type().isPrimitive()) {
            return;
         }

         this.error("can not assign " + this.getDest() + " of type " + this.getDest().type().typeName() + " a value of type " + this.sourceType().typeName());
      }

   }

   public Value eval(Body b) {
      TypeDecl dest = this.getDest().type();
      TypeDecl source = this.getSource().type();
      TypeDecl type;
      if (dest.isNumericType() && source.isNumericType()) {
         type = dest.binaryNumericPromotion(source);
      } else {
         type = dest;
      }

      Value lvalue = this.getDest().eval(b);
      Value v = lvalue instanceof Local ? lvalue : (Value)lvalue.clone();
      Value value = b.newTemp(dest.emitCastTo(b, v, type, this));
      Value rvalue = source.emitCastTo(b, this.getSource(), type);
      Value result = this.asImmediate(b, type.emitCastTo(b, this.createAssignOp(b, value, rvalue), dest, this.getDest()));
      this.getDest().emitStore(b, lvalue, result, this);
      return result;
   }

   public Value emitShiftExpr(Body b) {
      TypeDecl dest = this.getDest().type();
      TypeDecl source = this.getSource().type();
      TypeDecl type = dest.unaryNumericPromotion();
      Value lvalue = this.getDest().eval(b);
      Value v = lvalue instanceof Local ? lvalue : (Value)lvalue.clone();
      Value value = b.newTemp(dest.emitCastTo(b, v, type, this.getDest()));
      Value rvalue = source.emitCastTo(b, this.getSource(), this.typeInt());
      Value result = this.asImmediate(b, type.emitCastTo(b, this.createAssignOp(b, value, rvalue), dest, this.getDest()));
      this.getDest().emitStore(b, lvalue, result, this);
      return result;
   }

   public Value createAssignOp(Body b, Value fst, Value snd) {
      throw new Error("Operation createAssignOp is not implemented for " + this.getClass().getName());
   }

   public AssignExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AssignExpr(Expr p0, Expr p1) {
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

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getDest().isVariable() && this.getDest().varDecl() == v || this.getSource().isDAafter(v);
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v) || this.isFalse();
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v) || this.isTrue();
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getSource().isDUafter(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafter(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafter(v);
   }

   public String printOp() {
      ASTNode$State state = this.state();
      return " = ";
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
      return this.getDest().type();
   }

   public TypeDecl sourceType() {
      ASTNode$State state = this.state();
      return this.getSource().type().isPrimitive() ? this.getSource().type() : this.unknownType();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getDest().isVariable(var);
   }

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      if (caller == this.getSourceNoTransform()) {
         return false;
      } else {
         return caller == this.getDestNoTransform() ? true : this.getParent().Define_boolean_isDest(this, caller);
      }
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      if (caller == this.getSourceNoTransform()) {
         return true;
      } else {
         return caller == this.getDestNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getDestNoTransform()) {
         return this.isDAbefore(v);
      } else {
         return caller == this.getSourceNoTransform() ? this.getDest().isDAafter(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getDestNoTransform()) {
         return this.isDUbefore(v);
      } else {
         return caller == this.getSourceNoTransform() ? this.getDest().isDUafter(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getDestNoTransform() ? NameType.EXPRESSION_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
