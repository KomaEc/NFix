package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CatchClause extends ASTNode<ASTNode> implements Cloneable, VariableScope {
   protected Map parameterDeclaration_String_values;
   protected boolean typeThrowable_computed = false;
   protected TypeDecl typeThrowable_value;
   protected Map lookupVariable_String_values;
   protected Map reachableCatchClause_TypeDecl_values;

   public void flushCache() {
      super.flushCache();
      this.parameterDeclaration_String_values = null;
      this.typeThrowable_computed = false;
      this.typeThrowable_value = null;
      this.lookupVariable_String_values = null;
      this.reachableCatchClause_TypeDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public CatchClause clone() throws CloneNotSupportedException {
      CatchClause node = (CatchClause)super.clone();
      node.parameterDeclaration_String_values = null;
      node.typeThrowable_computed = false;
      node.typeThrowable_value = null;
      node.lookupVariable_String_values = null;
      node.reachableCatchClause_TypeDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public CatchClause() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public CatchClause(Block p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setBlock(Block node) {
      this.setChild(node, 0);
   }

   public Block getBlock() {
      return (Block)this.getChild(0);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(0);
   }

   public boolean handles(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      return false;
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
      return SimpleSet.emptySet;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return this.getBlock().modifiedInScope(var);
   }

   public TypeDecl typeThrowable() {
      if (this.typeThrowable_computed) {
         return this.typeThrowable_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeThrowable_value = this.getParent().Define_TypeDecl_typeThrowable(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeThrowable_computed = true;
         }

         return this.typeThrowable_value;
      }
   }

   public SimpleSet lookupVariable(String name) {
      if (this.lookupVariable_String_values == null) {
         this.lookupVariable_String_values = new HashMap(4);
      }

      if (this.lookupVariable_String_values.containsKey(name)) {
         return (SimpleSet)this.lookupVariable_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.lookupVariable_String_values.put(name, lookupVariable_String_value);
         }

         return lookupVariable_String_value;
      }
   }

   public boolean reachableCatchClause(TypeDecl exceptionType) {
      if (this.reachableCatchClause_TypeDecl_values == null) {
         this.reachableCatchClause_TypeDecl_values = new HashMap(4);
      }

      if (this.reachableCatchClause_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.reachableCatchClause_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean reachableCatchClause_TypeDecl_value = this.getParent().Define_boolean_reachableCatchClause(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.reachableCatchClause_TypeDecl_values.put(exceptionType, reachableCatchClause_TypeDecl_value);
         }

         return reachableCatchClause_TypeDecl_value;
      }
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public Collection<TypeDecl> caughtExceptions() {
      ASTNode$State state = this.state();
      Collection<TypeDecl> caughtExceptions_value = this.getParent().Define_Collection_TypeDecl__caughtExceptions(this, (ASTNode)null);
      return caughtExceptions_value;
   }

   public boolean reportUnreachable() {
      ASTNode$State state = this.state();
      boolean reportUnreachable_value = this.getParent().Define_boolean_reportUnreachable(this, (ASTNode)null);
      return reportUnreachable_value;
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getBlockNoTransform()) {
         SimpleSet set = this.parameterDeclaration(name);
         return !set.isEmpty() ? set : this.lookupVariable(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this;
   }

   public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
      return caller == this.getBlockNoTransform() ? false : this.getParent().Define_boolean_reportUnreachable(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
