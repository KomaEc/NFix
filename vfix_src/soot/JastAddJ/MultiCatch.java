package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;

public class MultiCatch extends CatchClause implements Cloneable {
   protected Map parameterDeclaration_String_values;

   public void flushCache() {
      super.flushCache();
      this.parameterDeclaration_String_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MultiCatch clone() throws CloneNotSupportedException {
      MultiCatch node = (MultiCatch)super.clone();
      node.parameterDeclaration_String_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MultiCatch copy() {
      try {
         MultiCatch node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MultiCatch fullCopy() {
      MultiCatch tree = this.copy();
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

   public void toString(StringBuffer sb) {
      sb.append("catch (");
      this.getParameter().toString(sb);
      sb.append(") ");
      this.getBlock().toString(sb);
   }

   void checkUnreachableStmt() {
      if (!this.getBlock().reachable() && this.reportUnreachable()) {
         this.error("the exception " + this.getParameter().type().fullName() + " is not thrown in the body of the try statement");
      }

   }

   public MultiCatch() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public MultiCatch(CatchParameterDeclaration p0, Block p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setParameter(CatchParameterDeclaration node) {
      this.setChild(node, 0);
   }

   public CatchParameterDeclaration getParameter() {
      return (CatchParameterDeclaration)this.getChild(0);
   }

   public CatchParameterDeclaration getParameterNoTransform() {
      return (CatchParameterDeclaration)this.getChildNoTransform(0);
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

   public boolean handles(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      CatchParameterDeclaration param = this.getParameter();

      for(int i = 0; i < param.getNumTypeAccess(); ++i) {
         TypeDecl type = param.getTypeAccess(i).type();
         if (!type.isUnknown() && exceptionType.instanceOf(type)) {
            return true;
         }
      }

      return false;
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

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return caller == this.getParameterNoTransform() ? this.parameterDeclaration(name) : super.Define_SimpleSet_lookupVariable(caller, child, name);
   }

   public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
      if (caller == this.getBlockNoTransform()) {
         boolean anyReachable = false;
         CatchParameterDeclaration param = this.getParameter();

         for(int i = 0; i < param.getNumTypeAccess(); ++i) {
            TypeDecl type = param.getTypeAccess(i).type();
            if (!this.reachableCatchClause(type)) {
               this.error("The exception type " + type.fullName() + " can not be caught by this multi-catch clause");
            } else {
               anyReachable = true;
            }
         }

         return anyReachable;
      } else {
         return this.getParent().Define_boolean_reachable(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
