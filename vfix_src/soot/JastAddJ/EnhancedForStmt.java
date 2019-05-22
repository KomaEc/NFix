package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.jimple.IntConstant;

public class EnhancedForStmt extends BranchTargetStmt implements Cloneable, VariableScope {
   protected Map targetOf_ContinueStmt_values;
   protected Map targetOf_BreakStmt_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean cond_label_computed = false;
   protected soot.jimple.Stmt cond_label_value;
   protected boolean update_label_computed = false;
   protected soot.jimple.Stmt update_label_value;
   protected boolean end_label_computed = false;
   protected soot.jimple.Stmt end_label_value;
   protected boolean extraLocalIndex_computed = false;
   protected int extraLocalIndex_value;

   public void flushCache() {
      super.flushCache();
      this.targetOf_ContinueStmt_values = null;
      this.targetOf_BreakStmt_values = null;
      this.canCompleteNormally_computed = false;
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.cond_label_computed = false;
      this.cond_label_value = null;
      this.update_label_computed = false;
      this.update_label_value = null;
      this.end_label_computed = false;
      this.end_label_value = null;
      this.extraLocalIndex_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public EnhancedForStmt clone() throws CloneNotSupportedException {
      EnhancedForStmt node = (EnhancedForStmt)super.clone();
      node.targetOf_ContinueStmt_values = null;
      node.targetOf_BreakStmt_values = null;
      node.canCompleteNormally_computed = false;
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.cond_label_computed = false;
      node.cond_label_value = null;
      node.update_label_computed = false;
      node.update_label_value = null;
      node.end_label_computed = false;
      node.end_label_value = null;
      node.extraLocalIndex_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public EnhancedForStmt copy() {
      try {
         EnhancedForStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public EnhancedForStmt fullCopy() {
      EnhancedForStmt tree = this.copy();
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
      if (!this.getExpr().type().isArrayDecl() && !this.getExpr().type().isIterable()) {
         this.error("type " + this.getExpr().type().name() + " of expression in foreach is neither array type nor java.lang.Iterable");
      } else if (this.getExpr().type().isArrayDecl() && !this.getExpr().type().componentType().assignConversionTo(this.getVariableDeclaration().type(), (Expr)null)) {
         this.error("parameter of type " + this.getVariableDeclaration().type().typeName() + " can not be assigned an element of type " + this.getExpr().type().componentType().typeName());
      } else if (this.getExpr().type().isIterable() && !this.getExpr().type().isUnknown()) {
         MethodDecl iterator = (MethodDecl)this.getExpr().type().memberMethods("iterator").iterator().next();
         MethodDecl next = (MethodDecl)iterator.type().memberMethods("next").iterator().next();
         TypeDecl componentType = next.type();
         if (!componentType.assignConversionTo(this.getVariableDeclaration().type(), (Expr)null)) {
            this.error("parameter of type " + this.getVariableDeclaration().type().typeName() + " can not be assigned an element of type " + componentType.typeName());
         }
      }

   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("for (");
      this.getVariableDeclaration().getModifiers().toString(s);
      this.getVariableDeclaration().getTypeAccess().toString(s);
      s.append(" " + this.getVariableDeclaration().name());
      s.append(" : ");
      this.getExpr().toString(s);
      s.append(") ");
      this.getStmt().toString(s);
   }

   public void jimplify2(Body b) {
      Local array;
      Local index;
      if (this.getExpr().type().isArrayDecl()) {
         array = this.asLocal(b, this.getExpr().eval(b));
         index = this.asLocal(b, IntConstant.v(0));
         Local parameter = b.newLocal(this.getVariableDeclaration().name(), this.getVariableDeclaration().type().getSootType());
         this.getVariableDeclaration().local = parameter;
         b.setLine(this);
         b.addLabel(this.cond_label());
         b.add(b.newIfStmt(b.newGeExpr(this.asImmediate(b, index), this.asImmediate(b, b.newLengthExpr(this.asImmediate(b, array), this)), this), this.end_label(), this));
         b.add(b.newAssignStmt(parameter, this.asRValue(b, this.getExpr().type().elementType().emitCastTo(b, this.asLocal(b, b.newArrayRef(array, index, this)), this.getVariableDeclaration().type(), this)), this));
         this.getStmt().jimplify2(b);
         b.addLabel(this.update_label());
         b.add(b.newAssignStmt(index, b.newAddExpr(index, IntConstant.v(1), this), this));
         b.add(b.newGotoStmt(this.cond_label(), this));
         b.addLabel(this.end_label());
      } else {
         array = this.asLocal(b, b.newInterfaceInvokeExpr(this.asLocal(b, this.getExpr().eval(b)), this.iteratorMethod().sootRef(), (java.util.List)(new ArrayList()), this));
         index = b.newLocal(this.getVariableDeclaration().name(), this.getVariableDeclaration().type().getSootType());
         this.getVariableDeclaration().local = index;
         b.addLabel(this.cond_label());
         b.add(b.newIfStmt(b.newEqExpr(this.asImmediate(b, b.newInterfaceInvokeExpr(array, this.hasNextMethod().sootRef(), (java.util.List)(new ArrayList()), this)), BooleanType.emitConstant(false), this), this.end_label(), this));
         b.add(b.newAssignStmt(index, this.nextMethod().type().emitCastTo(b, b.newInterfaceInvokeExpr(array, this.nextMethod().sootRef(), (java.util.List)(new ArrayList()), this), this.getVariableDeclaration().type(), this), this));
         this.getStmt().jimplify2(b);
         b.addLabel(this.update_label());
         b.add(b.newGotoStmt(this.cond_label(), this));
         b.addLabel(this.end_label());
      }

   }

   private MethodDecl iteratorMethod() {
      TypeDecl typeDecl = this.lookupType("java.lang", "Iterable");
      Iterator iter = typeDecl.memberMethods("iterator").iterator();

      MethodDecl m;
      do {
         if (!iter.hasNext()) {
            throw new Error("Could not find java.lang.Iterable.iterator()");
         }

         m = (MethodDecl)iter.next();
      } while(m.getNumParameter() != 0);

      return m;
   }

   private MethodDecl hasNextMethod() {
      TypeDecl typeDecl = this.lookupType("java.util", "Iterator");
      Iterator iter = typeDecl.memberMethods("hasNext").iterator();

      MethodDecl m;
      do {
         if (!iter.hasNext()) {
            throw new Error("Could not find java.util.Collection.hasNext()");
         }

         m = (MethodDecl)iter.next();
      } while(m.getNumParameter() != 0);

      return m;
   }

   private MethodDecl nextMethod() {
      TypeDecl typeDecl = this.lookupType("java.util", "Iterator");
      Iterator iter = typeDecl.memberMethods("next").iterator();

      MethodDecl m;
      do {
         if (!iter.hasNext()) {
            throw new Error("Could not find java.util.Collection.next()");
         }

         m = (MethodDecl)iter.next();
      } while(m.getNumParameter() != 0);

      return m;
   }

   public EnhancedForStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
   }

   public EnhancedForStmt(VariableDeclaration p0, Expr p1, Stmt p2) {
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

   public void setVariableDeclaration(VariableDeclaration node) {
      this.setChild(node, 0);
   }

   public VariableDeclaration getVariableDeclaration() {
      return (VariableDeclaration)this.getChild(0);
   }

   public VariableDeclaration getVariableDeclarationNoTransform() {
      return (VariableDeclaration)this.getChildNoTransform(0);
   }

   public void setExpr(Expr node) {
      this.setChild(node, 1);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(1);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(1);
   }

   public void setStmt(Stmt node) {
      this.setChild(node, 2);
   }

   public Stmt getStmt() {
      return (Stmt)this.getChild(2);
   }

   public Stmt getStmtNoTransform() {
      return (Stmt)this.getChildNoTransform(2);
   }

   public SimpleSet localLookupVariable(String name) {
      ASTNode$State state = this.state();
      return this.getVariableDeclaration().name().equals(name) ? SimpleSet.emptySet.add(this.getVariableDeclaration()) : this.lookupVariable(name);
   }

   public boolean targetOf(ContinueStmt stmt) {
      if (this.targetOf_ContinueStmt_values == null) {
         this.targetOf_ContinueStmt_values = new HashMap(4);
      }

      if (this.targetOf_ContinueStmt_values.containsKey(stmt)) {
         return (Boolean)this.targetOf_ContinueStmt_values.get(stmt);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean targetOf_ContinueStmt_value = this.targetOf_compute(stmt);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetOf_ContinueStmt_values.put(stmt, targetOf_ContinueStmt_value);
         }

         return targetOf_ContinueStmt_value;
      }
   }

   private boolean targetOf_compute(ContinueStmt stmt) {
      return !stmt.hasLabel();
   }

   public boolean targetOf(BreakStmt stmt) {
      if (this.targetOf_BreakStmt_values == null) {
         this.targetOf_BreakStmt_values = new HashMap(4);
      }

      if (this.targetOf_BreakStmt_values.containsKey(stmt)) {
         return (Boolean)this.targetOf_BreakStmt_values.get(stmt);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean targetOf_BreakStmt_value = this.targetOf_compute(stmt);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.targetOf_BreakStmt_values.put(stmt, targetOf_BreakStmt_value);
         }

         return targetOf_BreakStmt_value;
      }
   }

   private boolean targetOf_compute(BreakStmt stmt) {
      return !stmt.hasLabel();
   }

   public boolean canCompleteNormally() {
      if (this.canCompleteNormally_computed) {
         return this.canCompleteNormally_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.canCompleteNormally_value = this.canCompleteNormally_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.canCompleteNormally_computed = true;
         }

         return this.canCompleteNormally_value;
      }
   }

   private boolean canCompleteNormally_compute() {
      return this.reachable();
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
      return this.getExpr().isDAafter(v);
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
      if (!this.getExpr().isDUafter(v)) {
         return false;
      } else {
         Iterator iter = this.targetBreaks().iterator();

         BreakStmt stmt;
         do {
            if (!iter.hasNext()) {
               return true;
            }

            stmt = (BreakStmt)iter.next();
         } while(stmt.isDUafterReachedFinallyBlocks(v));

         return false;
      }
   }

   public boolean continueLabel() {
      ASTNode$State state = this.state();
      return true;
   }

   public soot.jimple.Stmt cond_label() {
      if (this.cond_label_computed) {
         return this.cond_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.cond_label_value = this.cond_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.cond_label_computed = true;
         }

         return this.cond_label_value;
      }
   }

   private soot.jimple.Stmt cond_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt update_label() {
      if (this.update_label_computed) {
         return this.update_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.update_label_value = this.update_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.update_label_computed = true;
         }

         return this.update_label_value;
      }
   }

   private soot.jimple.Stmt update_label_compute() {
      return this.newLabel();
   }

   public soot.jimple.Stmt end_label() {
      if (this.end_label_computed) {
         return this.end_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.end_label_value = this.end_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.end_label_computed = true;
         }

         return this.end_label_value;
      }
   }

   private soot.jimple.Stmt end_label_compute() {
      return this.newLabel();
   }

   public int extraLocalIndex() {
      if (this.extraLocalIndex_computed) {
         return this.extraLocalIndex_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.extraLocalIndex_value = this.extraLocalIndex_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.extraLocalIndex_computed = true;
         }

         return this.extraLocalIndex_value;
      }
   }

   private int extraLocalIndex_compute() {
      return this.localNum();
   }

   public soot.jimple.Stmt break_label() {
      ASTNode$State state = this.state();
      return this.end_label();
   }

   public soot.jimple.Stmt continue_label() {
      ASTNode$State state = this.state();
      return this.update_label();
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getStmt().modifiedInScope(var);
   }

   public SimpleSet lookupVariable(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getStmtNoTransform()) {
         return this.localLookupVariable(name);
      } else if (caller == this.getExprNoTransform()) {
         return this.localLookupVariable(name);
      } else {
         return caller == this.getVariableDeclarationNoTransform() ? this.localLookupVariable(name) : this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getVariableDeclarationNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtNoTransform()) {
         return this;
      } else if (caller == this.getExprNoTransform()) {
         return this;
      } else {
         return (VariableScope)(caller == this.getVariableDeclarationNoTransform() ? this : this.getParent().Define_VariableScope_outerScope(this, caller));
      }
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      return caller == this.getVariableDeclarationNoTransform() ? false : this.getParent().Define_boolean_isMethodParameter(this, caller);
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      return caller == this.getVariableDeclarationNoTransform() ? false : this.getParent().Define_boolean_isConstructorParameter(this, caller);
   }

   public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
      return caller == this.getVariableDeclarationNoTransform() ? false : this.getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? this.reachable() : this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtNoTransform()) {
         return this.getExpr().isDAafter(v);
      } else if (caller != this.getExprNoTransform()) {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      } else {
         return v == this.getVariableDeclaration() || this.isDAbefore(v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getStmtNoTransform()) {
         return this.getExpr().isDUafter(v);
      } else if (caller != this.getExprNoTransform()) {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      } else {
         return v != this.getVariableDeclaration() && this.isDUbefore(v);
      }
   }

   public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
      return caller == this.getStmtNoTransform() ? true : this.getParent().Define_boolean_insideLoop(this, caller);
   }

   public int Define_int_localNum(ASTNode caller, ASTNode child) {
      if (caller == this.getStmtNoTransform()) {
         return this.getVariableDeclaration().localNum() + this.getVariableDeclaration().type().size();
      } else {
         return caller == this.getVariableDeclarationNoTransform() ? this.localNum() + (this.getExpr().type().isArrayDecl() ? 2 : 1) : this.getParent().Define_int_localNum(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
