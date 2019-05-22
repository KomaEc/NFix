package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.Local;

public class BasicCatch extends CatchClause implements Cloneable {
   protected Map parameterDeclaration_String_values;
   protected boolean label_computed = false;
   protected soot.jimple.Stmt label_value;

   public void flushCache() {
      super.flushCache();
      this.parameterDeclaration_String_values = null;
      this.label_computed = false;
      this.label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BasicCatch clone() throws CloneNotSupportedException {
      BasicCatch node = (BasicCatch)super.clone();
      node.parameterDeclaration_String_values = null;
      node.label_computed = false;
      node.label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BasicCatch copy() {
      try {
         BasicCatch node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BasicCatch fullCopy() {
      BasicCatch tree = this.copy();
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
      s.append("catch (");
      this.getParameter().toString(s);
      s.append(") ");
      this.getBlock().toString(s);
   }

   public void typeCheck() {
      if (!this.getParameter().type().instanceOf(this.typeThrowable())) {
         this.error("*** The catch variable must extend Throwable");
      }

   }

   public void jimplify2(Body b) {
      b.addLabel(this.label());
      Local local = b.newLocal(this.getParameter().name(), this.getParameter().type().getSootType());
      b.setLine(this);
      b.add(b.newIdentityStmt(local, b.newCaughtExceptionRef(this.getParameter()), this));
      this.getParameter().local = local;
      this.getBlock().jimplify2(b);
   }

   void checkUnreachableStmt() {
      if (!this.getBlock().reachable() && this.reportUnreachable()) {
         this.error("the exception " + this.getParameter().type().fullName() + " is not thrown in the body of the try statement");
      }

   }

   public BasicCatch() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public BasicCatch(ParameterDeclaration p0, Block p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setParameter(ParameterDeclaration node) {
      this.setChild(node, 0);
   }

   public ParameterDeclaration getParameter() {
      return (ParameterDeclaration)this.getChild(0);
   }

   public ParameterDeclaration getParameterNoTransform() {
      return (ParameterDeclaration)this.getChildNoTransform(0);
   }

   public void setBlock(Block node) {
      this.setChild(node, 1);
   }

   public Block getBlock() {
      return (Block)this.getChild(1);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(1);
   }

   public boolean handles(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      return !this.getParameter().type().isUnknown() && exceptionType.instanceOf(this.getParameter().type());
   }

   public SimpleSet parameterDeclaration(String name) {
      if (this.parameterDeclaration_String_values == null) {
         this.parameterDeclaration_String_values = new HashMap(4);
      }

      if (this.parameterDeclaration_String_values.containsKey(name)) {
         return (SimpleSet)this.parameterDeclaration_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet parameterDeclaration_String_value = this.parameterDeclaration_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.parameterDeclaration_String_values.put(name, parameterDeclaration_String_value);
         }

         return parameterDeclaration_String_value;
      }
   }

   private SimpleSet parameterDeclaration_compute(String name) {
      return (SimpleSet)(this.getParameter().name().equals(name) ? this.getParameter() : SimpleSet.emptySet);
   }

   public soot.jimple.Stmt label() {
      if (this.label_computed) {
         return this.label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.label_value = this.label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.label_computed = true;
         }

         return this.label_value;
      }
   }

   private soot.jimple.Stmt label_compute() {
      return this.newLabel();
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return caller == this.getParameterNoTransform() ? this.parameterDeclaration(name) : super.Define_SimpleSet_lookupVariable(caller, child, name);
   }

   public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
      return (VariableScope)(caller == this.getParameterNoTransform() ? this : this.getParent().Define_VariableScope_outerScope(this, caller));
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? this.reachableCatchClause(this.getParameter().type()) : this.getParent().Define_boolean_reachable(this, caller);
   }

   public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? false : this.getParent().Define_boolean_isMethodParameter(this, caller);
   }

   public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? false : this.getParent().Define_boolean_isConstructorParameter(this, caller);
   }

   public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? true : this.getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
   }

   public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? false : this.getParent().Define_boolean_variableArityValid(this, caller);
   }

   public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
      return caller == this.getParameterNoTransform() ? this.getBlock().modifiedInScope(var) : this.getParent().Define_boolean_inhModifiedInScope(this, caller, var);
   }

   public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
      return caller == this.getParameterNoTransform() ? true : this.getParent().Define_boolean_isCatchParam(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
