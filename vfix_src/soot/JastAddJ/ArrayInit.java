package soot.JastAddJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import soot.Local;
import soot.Value;

public class ArrayInit extends Expr implements Cloneable {
   protected Map computeDABefore_int_Variable_values;
   protected Map computeDUbefore_int_Variable_values;
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected boolean declType_computed = false;
   protected TypeDecl declType_value;

   public void flushCache() {
      super.flushCache();
      this.computeDABefore_int_Variable_values = null;
      this.computeDUbefore_int_Variable_values = null;
      this.type_computed = false;
      this.type_value = null;
      this.declType_computed = false;
      this.declType_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ArrayInit clone() throws CloneNotSupportedException {
      ArrayInit node = (ArrayInit)super.clone();
      node.computeDABefore_int_Variable_values = null;
      node.computeDUbefore_int_Variable_values = null;
      node.type_computed = false;
      node.type_value = null;
      node.declType_computed = false;
      node.declType_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ArrayInit copy() {
      try {
         ArrayInit node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ArrayInit fullCopy() {
      ArrayInit tree = this.copy();
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
      s.append("{ ");
      if (this.getNumInit() > 0) {
         this.getInit(0).toString(s);

         for(int i = 1; i < this.getNumInit(); ++i) {
            s.append(", ");
            this.getInit(i).toString(s);
         }
      }

      s.append(" } ");
   }

   public void typeCheck() {
      TypeDecl initializerType = this.declType().componentType();
      if (initializerType.isUnknown()) {
         this.error("the dimension of the initializer is larger than the expected dimension");
      }

      for(int i = 0; i < this.getNumInit(); ++i) {
         Expr e = this.getInit(i);
         if (!e.type().assignConversionTo(initializerType, e)) {
            this.error("the type " + e.type().name() + " of the initializer is not compatible with " + initializerType.name());
         }
      }

   }

   public Value eval(Body b) {
      Value size = IntType.emitConstant(this.getNumInit());
      Local array = this.asLocal(b, b.newNewArrayExpr(this.type().componentType().getSootType(), this.asImmediate(b, size), this));

      for(int i = 0; i < this.getNumInit(); ++i) {
         Value rvalue = this.getInit(i).type().emitCastTo(b, this.getInit(i), this.expectedType());
         Value index = IntType.emitConstant(i);
         Value lvalue = b.newArrayRef(array, index, this.getInit(i));
         b.setLine(this);
         b.add(b.newAssignStmt(lvalue, this.asImmediate(b, rvalue), this.getInit(i)));
      }

      return array;
   }

   public ArrayInit() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public ArrayInit(List<Expr> p0) {
      this.setChild(p0, 0);
   }

   protected int numChildren() {
      return 1;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setInitList(List<Expr> list) {
      this.setChild(list, 0);
   }

   public int getNumInit() {
      return this.getInitList().getNumChild();
   }

   public int getNumInitNoTransform() {
      return this.getInitListNoTransform().getNumChildNoTransform();
   }

   public Expr getInit(int i) {
      return (Expr)this.getInitList().getChild(i);
   }

   public void addInit(Expr node) {
      List<Expr> list = this.parent != null && state != null ? this.getInitList() : this.getInitListNoTransform();
      list.addChild(node);
   }

   public void addInitNoTransform(Expr node) {
      List<Expr> list = this.getInitListNoTransform();
      list.addChild(node);
   }

   public void setInit(Expr node, int i) {
      List<Expr> list = this.getInitList();
      list.setChild(node, i);
   }

   public List<Expr> getInits() {
      return this.getInitList();
   }

   public List<Expr> getInitsNoTransform() {
      return this.getInitListNoTransform();
   }

   public List<Expr> getInitList() {
      List<Expr> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Expr> getInitListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public boolean representableIn(TypeDecl t) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumInit(); ++i) {
         if (!this.getInit(i).representableIn(t)) {
            return false;
         }
      }

      return true;
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumInit() == 0 ? this.isDAbefore(v) : this.getInit(this.getNumInit() - 1).isDAafter(v);
   }

   public boolean computeDABefore(int childIndex, Variable v) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(childIndex);
      _parameters.add(v);
      if (this.computeDABefore_int_Variable_values == null) {
         this.computeDABefore_int_Variable_values = new HashMap(4);
      }

      if (this.computeDABefore_int_Variable_values.containsKey(_parameters)) {
         return (Boolean)this.computeDABefore_int_Variable_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean computeDABefore_int_Variable_value = this.computeDABefore_compute(childIndex, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.computeDABefore_int_Variable_values.put(_parameters, computeDABefore_int_Variable_value);
         }

         return computeDABefore_int_Variable_value;
      }
   }

   private boolean computeDABefore_compute(int childIndex, Variable v) {
      if (childIndex == 0) {
         return this.isDAbefore(v);
      } else {
         for(int index = childIndex - 1; index > 0 && this.getInit(index).isConstant(); --index) {
         }

         return this.getInit(childIndex - 1).isDAafter(v);
      }
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumInit() == 0 ? this.isDUbefore(v) : this.getInit(this.getNumInit() - 1).isDUafter(v);
   }

   public boolean computeDUbefore(int childIndex, Variable v) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(childIndex);
      _parameters.add(v);
      if (this.computeDUbefore_int_Variable_values == null) {
         this.computeDUbefore_int_Variable_values = new HashMap(4);
      }

      if (this.computeDUbefore_int_Variable_values.containsKey(_parameters)) {
         return (Boolean)this.computeDUbefore_int_Variable_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean computeDUbefore_int_Variable_value = this.computeDUbefore_compute(childIndex, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.computeDUbefore_int_Variable_values.put(_parameters, computeDUbefore_int_Variable_value);
         }

         return computeDUbefore_int_Variable_value;
      }
   }

   private boolean computeDUbefore_compute(int childIndex, Variable v) {
      if (childIndex == 0) {
         return this.isDUbefore(v);
      } else {
         for(int index = childIndex - 1; index > 0 && this.getInit(index).isConstant(); --index) {
         }

         return this.getInit(childIndex - 1).isDUafter(v);
      }
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
      return this.declType();
   }

   public TypeDecl declType() {
      if (this.declType_computed) {
         return this.declType_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.declType_value = this.getParent().Define_TypeDecl_declType(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.declType_computed = true;
         }

         return this.declType_value;
      }
   }

   public TypeDecl expectedType() {
      ASTNode$State state = this.state();
      TypeDecl expectedType_value = this.getParent().Define_TypeDecl_expectedType(this, (ASTNode)null);
      return expectedType_value;
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      if (caller == this.getInitListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_isSource(this, caller);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getInitListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);
         return this.computeDABefore(childIndex, v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getInitListNoTransform()) {
         int childIndex = caller.getIndexOfChild(child);
         return this.computeDUbefore(childIndex, v);
      } else {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
      }
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      if (caller == this.getInitListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.declType().componentType();
      } else {
         return this.getParent().Define_TypeDecl_declType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      if (caller == this.getInitListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.declType().componentType();
      } else {
         return this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
      }
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      if (caller == this.getInitListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.expectedType().componentType();
      } else {
         return this.getParent().Define_TypeDecl_expectedType(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
