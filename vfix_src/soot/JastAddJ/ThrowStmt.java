package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThrowStmt extends Stmt implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean canCompleteNormally_computed = false;
   protected boolean canCompleteNormally_value;
   protected boolean typeNullPointerException_computed = false;
   protected TypeDecl typeNullPointerException_value;
   protected Map handlesException_TypeDecl_values;
   protected boolean typeThrowable_computed = false;
   protected TypeDecl typeThrowable_value;
   protected boolean typeNull_computed = false;
   protected TypeDecl typeNull_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.canCompleteNormally_computed = false;
      this.typeNullPointerException_computed = false;
      this.typeNullPointerException_value = null;
      this.handlesException_TypeDecl_values = null;
      this.typeThrowable_computed = false;
      this.typeThrowable_value = null;
      this.typeNull_computed = false;
      this.typeNull_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ThrowStmt clone() throws CloneNotSupportedException {
      ThrowStmt node = (ThrowStmt)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.canCompleteNormally_computed = false;
      node.typeNullPointerException_computed = false;
      node.typeNullPointerException_value = null;
      node.handlesException_TypeDecl_values = null;
      node.typeThrowable_computed = false;
      node.typeThrowable_value = null;
      node.typeNull_computed = false;
      node.typeNull_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ThrowStmt copy() {
      try {
         ThrowStmt node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ThrowStmt fullCopy() {
      ThrowStmt tree = this.copy();
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

   protected void collectExceptions(Collection c, ASTNode target) {
      super.collectExceptions(c, target);
      TypeDecl exceptionType = this.getExpr().type();
      if (exceptionType == this.typeNull()) {
         exceptionType = this.typeNullPointerException();
      }

      c.add(exceptionType);
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      s.append("throw ");
      this.getExpr().toString(s);
      s.append(";");
   }

   public void typeCheck() {
      if (!this.getExpr().type().instanceOf(this.typeThrowable())) {
         this.error("*** The thrown expression must extend Throwable");
      }

   }

   public void jimplify2(Body b) {
      b.setLine(this);
      b.add(b.newThrowStmt(this.asImmediate(b, this.getExpr().eval(b)), this));
   }

   public ThrowStmt() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
   }

   public ThrowStmt(Expr p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setExpr(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getExpr() {
      return (Expr)this.getChild(0);
   }

   public Expr getExprNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void exceptionHandling() {
      Collection<TypeDecl> exceptionTypes = this.getExpr().throwTypes();
      Iterator var2 = exceptionTypes.iterator();

      while(var2.hasNext()) {
         TypeDecl exceptionType = (TypeDecl)var2.next();
         if (exceptionType == this.typeNull()) {
            exceptionType = this.typeNullPointerException();
         }

         if (!this.handlesException(exceptionType)) {
            this.error("" + this + " throws uncaught exception " + exceptionType.fullName());
         }
      }

   }

   protected boolean reachedException(TypeDecl catchType) {
      Collection<TypeDecl> exceptionTypes = this.getExpr().throwTypes();
      boolean reached = false;
      Iterator var4 = exceptionTypes.iterator();

      while(var4.hasNext()) {
         TypeDecl exceptionType = (TypeDecl)var4.next();
         if (exceptionType == this.typeNull()) {
            exceptionType = this.typeNullPointerException();
         }

         if (catchType.mayCatch(exceptionType)) {
            reached = true;
            break;
         }

         if (super.reachedException(catchType)) {
            reached = true;
            break;
         }
      }

      return reached;
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
      return false;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public TypeDecl typeNullPointerException() {
      if (this.typeNullPointerException_computed) {
         return this.typeNullPointerException_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeNullPointerException_value = this.getParent().Define_TypeDecl_typeNullPointerException(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeNullPointerException_computed = true;
         }

         return this.typeNullPointerException_value;
      }
   }

   public boolean handlesException(TypeDecl exceptionType) {
      if (this.handlesException_TypeDecl_values == null) {
         this.handlesException_TypeDecl_values = new HashMap(4);
      }

      if (this.handlesException_TypeDecl_values.containsKey(exceptionType)) {
         return (Boolean)this.handlesException_TypeDecl_values.get(exceptionType);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.handlesException_TypeDecl_values.put(exceptionType, handlesException_TypeDecl_value);
         }

         return handlesException_TypeDecl_value;
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

   public TypeDecl typeNull() {
      if (this.typeNull_computed) {
         return this.typeNull_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.typeNull_value = this.getParent().Define_TypeDecl_typeNull(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeNull_computed = true;
         }

         return this.typeNull_value;
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getExprNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getExprNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
