package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BodyDecl extends ASTNode<ASTNode> implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected Map isDAbefore_Variable_values;
   protected Map isDUbefore_Variable_values;
   protected boolean typeThrowable_computed = false;
   protected TypeDecl typeThrowable_value;
   protected Map lookupVariable_String_values;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.isDAbefore_Variable_values = null;
      this.isDUbefore_Variable_values = null;
      this.typeThrowable_computed = false;
      this.typeThrowable_value = null;
      this.lookupVariable_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BodyDecl clone() throws CloneNotSupportedException {
      BodyDecl node = (BodyDecl)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.isDAbefore_Variable_values = null;
      node.isDUbefore_Variable_values = null;
      node.typeThrowable_computed = false;
      node.typeThrowable_value = null;
      node.lookupVariable_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public void collectFinally(Stmt branchStmt, ArrayList list) {
   }

   public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
      throw new Error("Operation substitutedBodyDecl not supported for " + this.getClass().getName());
   }

   public void jimplify1phase2() {
   }

   public void jimplify2() {
   }

   public void checkWarnings() {
      if (this.hasIllegalAnnotationSafeVarargs()) {
         this.error("@SafeVarargs is only allowed for variable arity method and constructor declarations");
      }

   }

   public void init$Children() {
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
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
      return true;
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
      return true;
   }

   public boolean declaresType(String name) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl type(String name) {
      ASTNode$State state = this.state();
      return null;
   }

   public boolean addsIndentationLevel() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isVoid() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDeprecated() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isEnumConstant() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean visibleTypeParameters() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean generate() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean hasAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean hasIllegalAnnotationSafeVarargs() {
      ASTNode$State state = this.state();
      return this.hasAnnotationSafeVarargs();
   }

   public boolean isDAbefore(Variable v) {
      if (this.isDAbefore_Variable_values == null) {
         this.isDAbefore_Variable_values = new HashMap(4);
      }

      if (this.isDAbefore_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAbefore_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAbefore_Variable_value = this.getParent().Define_boolean_isDAbefore(this, (ASTNode)null, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAbefore_Variable_values.put(v, isDAbefore_Variable_value);
         }

         return isDAbefore_Variable_value;
      }
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

   public Collection lookupMethod(String name) {
      ASTNode$State state = this.state();
      Collection lookupMethod_String_value = this.getParent().Define_Collection_lookupMethod(this, (ASTNode)null, name);
      return lookupMethod_String_value;
   }

   public TypeDecl lookupType(String packageName, String typeName) {
      ASTNode$State state = this.state();
      TypeDecl lookupType_String_String_value = this.getParent().Define_TypeDecl_lookupType(this, (ASTNode)null, packageName, typeName);
      return lookupType_String_String_value;
   }

   public SimpleSet lookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
      return lookupType_String_value;
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

   public NameType nameType() {
      ASTNode$State state = this.state();
      NameType nameType_value = this.getParent().Define_NameType_nameType(this, (ASTNode)null);
      return nameType_value;
   }

   public String hostPackage() {
      ASTNode$State state = this.state();
      String hostPackage_value = this.getParent().Define_String_hostPackage(this, (ASTNode)null);
      return hostPackage_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.indent();
   }

   public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this;
   }

   public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return null;
   }

   public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
