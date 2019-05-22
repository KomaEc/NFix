package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.Local;
import soot.Scene;
import soot.Value;

public abstract class Access extends Expr implements Cloneable {
   protected boolean prevExpr_computed = false;
   protected Expr prevExpr_value;
   protected boolean hasPrevExpr_computed = false;
   protected boolean hasPrevExpr_value;
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.prevExpr_computed = false;
      this.prevExpr_value = null;
      this.hasPrevExpr_computed = false;
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Access clone() throws CloneNotSupportedException {
      Access node = (Access)super.clone();
      node.prevExpr_computed = false;
      node.prevExpr_value = null;
      node.hasPrevExpr_computed = false;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public Access addArrayDims(List list) {
      Access a = this;

      for(int i = 0; i < list.getNumChildNoTransform(); ++i) {
         Dims dims = (Dims)list.getChildNoTransform(i);
         Opt opt = dims.getExprOpt();
         if (opt.getNumChildNoTransform() == 1) {
            a = new ArrayTypeWithSizeAccess((Access)a, (Expr)opt.getChildNoTransform(0));
         } else {
            a = new ArrayTypeAccess((Access)a);
         }

         ((Access)a).setStart(dims.start());
         ((Access)a).setEnd(dims.end());
      }

      return (Access)a;
   }

   protected TypeDecl superConstructorQualifier(TypeDecl targetEnclosingType) {
      TypeDecl enclosing;
      for(enclosing = this.hostType(); !enclosing.instanceOf(targetEnclosingType); enclosing = enclosing.enclosingType()) {
      }

      return enclosing;
   }

   public Value emitLoadLocalInNestedClass(Body b, Variable v) {
      if (this.inExplicitConstructorInvocation() && this.enclosingBodyDecl() instanceof ConstructorDecl) {
         ConstructorDecl c = (ConstructorDecl)this.enclosingBodyDecl();
         return ((ParameterDeclaration)c.parameterDeclaration(v.name()).iterator().next()).local;
      } else {
         return b.newInstanceFieldRef(b.emitThis(this.hostType()), Scene.v().makeFieldRef(this.hostType().getSootClassDecl(), "val$" + v.name(), v.type().getSootType(), false), this);
      }
   }

   public Local emitThis(Body b, TypeDecl targetDecl) {
      b.setLine(this);
      if (targetDecl == this.hostType()) {
         return b.emitThis(this.hostType());
      } else {
         TypeDecl enclosing = this.hostType();
         Local base;
         if (this.inExplicitConstructorInvocation()) {
            base = this.asLocal(b, b.newParameterRef(enclosing.enclosingType().getSootType(), 0, this));
            enclosing = enclosing.enclosing();
         } else {
            base = b.emitThis(this.hostType());
         }

         while(enclosing != targetDecl) {
            Local next = b.newTemp(enclosing.enclosingType().getSootType());
            b.add(b.newAssignStmt(next, b.newInstanceFieldRef(base, enclosing.getSootField("this$0", enclosing.enclosingType()).makeRef(), this), this));
            base = next;
            enclosing = enclosing.enclosingType();
         }

         return base;
      }
   }

   public void addArraySize(Body b, ArrayList list) {
   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public Expr unqualifiedScope() {
      ASTNode$State state = this.state();
      return (Expr)(this.isQualified() ? this.nestedScope() : this);
   }

   public boolean isQualified() {
      ASTNode$State state = this.state();
      return this.hasPrevExpr();
   }

   public Expr qualifier() {
      ASTNode$State state = this.state();
      return this.prevExpr();
   }

   public Access lastAccess() {
      ASTNode$State state = this.state();
      return this;
   }

   public Expr prevExpr() {
      if (this.prevExpr_computed) {
         return this.prevExpr_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.prevExpr_value = this.prevExpr_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.prevExpr_computed = true;
         }

         return this.prevExpr_value;
      }
   }

   private Expr prevExpr_compute() {
      if (this.isLeftChildOfDot()) {
         if (this.parentDot().isRightChildOfDot()) {
            return this.parentDot().parentDot().leftSide();
         }
      } else if (this.isRightChildOfDot()) {
         return this.parentDot().leftSide();
      }

      throw new Error(this + " does not have a previous expression");
   }

   public boolean hasPrevExpr() {
      if (this.hasPrevExpr_computed) {
         return this.hasPrevExpr_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.hasPrevExpr_value = this.hasPrevExpr_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.hasPrevExpr_computed = true;
         }

         return this.hasPrevExpr_value;
      }
   }

   private boolean hasPrevExpr_compute() {
      if (this.isLeftChildOfDot()) {
         if (this.parentDot().isRightChildOfDot()) {
            return true;
         }
      } else if (this.isRightChildOfDot()) {
         return true;
      }

      return false;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.NO_NAME;
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
      return this.unknownType();
   }

   public boolean isDiamond() {
      ASTNode$State state = this.state();
      return false;
   }

   public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
      ASTNode$State state = this.state();
      return (Access)this.cloneSubtree();
   }

   public Expr nestedScope() {
      ASTNode$State state = this.state();
      Expr nestedScope_value = this.getParent().Define_Expr_nestedScope(this, (ASTNode)null);
      return nestedScope_value;
   }

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public Variable unknownField() {
      ASTNode$State state = this.state();
      Variable unknownField_value = this.getParent().Define_Variable_unknownField(this, (ASTNode)null);
      return unknownField_value;
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean withinDeprecatedAnnotation() {
      ASTNode$State state = this.state();
      boolean withinDeprecatedAnnotation_value = this.getParent().Define_boolean_withinDeprecatedAnnotation(this, (ASTNode)null);
      return withinDeprecatedAnnotation_value;
   }

   public boolean inExplicitConstructorInvocation() {
      ASTNode$State state = this.state();
      boolean inExplicitConstructorInvocation_value = this.getParent().Define_boolean_inExplicitConstructorInvocation(this, (ASTNode)null);
      return inExplicitConstructorInvocation_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
