package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.Value;

public class AbstractDot extends Access implements Cloneable {
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected Map isDUbefore_Variable_values;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.type_computed = false;
      this.type_value = null;
      this.isDUbefore_Variable_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public AbstractDot clone() throws CloneNotSupportedException {
      AbstractDot node = (AbstractDot)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.type_computed = false;
      node.type_value = null;
      node.isDUbefore_Variable_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public AbstractDot copy() {
      try {
         AbstractDot node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public AbstractDot fullCopy() {
      AbstractDot tree = this.copy();
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
      this.getLeft().toString(s);
      if (!this.nextAccess().isArrayAccess()) {
         s.append(".");
      }

      this.getRight().toString(s);
   }

   public Access extractLast() {
      return this.getRightNoTransform();
   }

   public void replaceLast(Access access) {
      this.setRight(access);
   }

   public void emitEvalBranch(Body b) {
      this.lastAccess().emitEvalBranch(b);
   }

   public Value eval(Body b) {
      return this.lastAccess().eval(b);
   }

   public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
      return this.lastAccess().emitStore(b, lvalue, rvalue, location);
   }

   public AbstractDot() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public AbstractDot(Expr p0, Access p1) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
   }

   protected int numChildren() {
      return 2;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLeft(Expr node) {
      this.setChild(node, 0);
   }

   public Expr getLeft() {
      return (Expr)this.getChild(0);
   }

   public Expr getLeftNoTransform() {
      return (Expr)this.getChildNoTransform(0);
   }

   public void setRight(Access node) {
      this.setChild(node, 1);
   }

   public Access getRight() {
      return (Access)this.getChild(1);
   }

   public Access getRightNoTransform() {
      return (Access)this.getChildNoTransform(1);
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.lastAccess().constant();
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return this.lastAccess().isConstant();
   }

   public Variable varDecl() {
      ASTNode$State state = this.state();
      return this.lastAccess().varDecl();
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v);
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafter(v);
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
      return this.lastAccess().isDAafter(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafter(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafter(v);
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
      return this.lastAccess().isDUafter(v);
   }

   public String typeName() {
      ASTNode$State state = this.state();
      return this.lastAccess().typeName();
   }

   public boolean isTypeAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isTypeAccess();
   }

   public boolean isMethodAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isMethodAccess();
   }

   public boolean isFieldAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isFieldAccess();
   }

   public boolean isSuperAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isSuperAccess();
   }

   public boolean isThisAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isThisAccess();
   }

   public boolean isPackageAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isPackageAccess();
   }

   public boolean isArrayAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isArrayAccess();
   }

   public boolean isClassAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isClassAccess();
   }

   public boolean isSuperConstructorAccess() {
      ASTNode$State state = this.state();
      return this.getRight().isSuperConstructorAccess();
   }

   public boolean isQualified() {
      ASTNode$State state = this.state();
      return this.hasParentDot();
   }

   public Expr leftSide() {
      ASTNode$State state = this.state();
      return this.getLeft();
   }

   public Access rightSide() {
      ASTNode$State state = this.state();
      return this.getRight() instanceof AbstractDot ? (Access)((AbstractDot)this.getRight()).getLeft() : this.getRight();
   }

   public Access lastAccess() {
      ASTNode$State state = this.state();
      return this.getRight().lastAccess();
   }

   public Access nextAccess() {
      ASTNode$State state = this.state();
      return this.rightSide();
   }

   public Expr prevExpr() {
      ASTNode$State state = this.state();
      return this.leftSide();
   }

   public boolean hasPrevExpr() {
      ASTNode$State state = this.state();
      return true;
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return this.getLeft() instanceof Access ? ((Access)this.getLeft()).predNameType() : NameType.NO_NAME;
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
      return this.lastAccess().type();
   }

   public boolean isVariable() {
      ASTNode$State state = this.state();
      return this.lastAccess().isVariable();
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return this.lastAccess().staticContextQualifier();
   }

   public boolean definesLabel() {
      ASTNode$State state = this.state();
      return this.getParent().definesLabel();
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return this.lastAccess().canBeTrue();
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return this.lastAccess().canBeFalse();
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

   public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
      return caller == this.getLeftNoTransform() ? false : this.getParent().Define_boolean_isDest(this, caller);
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getLeftNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getRightNoTransform() ? this.getLeft().isDAafter(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getRightNoTransform() ? this.getLeft().isDUafter(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
      return caller == this.getRightNoTransform() ? this.getLeft().type().constructors() : this.getParent().Define_Collection_lookupConstructor(this, caller);
   }

   public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
      return caller == this.getRightNoTransform() ? this.getLeft().type().lookupSuperConstructor() : this.getParent().Define_Collection_lookupSuperConstructor(this, caller);
   }

   public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
      if (caller == this.getLeftNoTransform()) {
         return (Expr)(this.isQualified() ? this.nestedScope() : this);
      } else if (caller == this.getRightNoTransform()) {
         return (Expr)(this.isQualified() ? this.nestedScope() : this);
      } else {
         return this.getParent().Define_Expr_nestedScope(this, caller);
      }
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      return caller == this.getRightNoTransform() ? this.getLeft().type().memberMethods(name) : this.getParent().Define_Collection_lookupMethod(this, caller, name);
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      return caller == this.getRightNoTransform() ? this.getLeft().hasQualifiedPackage(packageName) : this.getParent().Define_boolean_hasPackage(this, caller, packageName);
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      return caller == this.getRightNoTransform() ? this.getLeft().qualifiedLookupType(name) : this.getParent().Define_SimpleSet_lookupType(this, caller, name);
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      return caller == this.getRightNoTransform() ? this.getLeft().qualifiedLookupVariable(name) : this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getLeftNoTransform() ? this.getRight().predNameType() : this.getParent().Define_NameType_nameType(this, caller);
   }

   public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
      return caller == this.getRightNoTransform() ? this.getLeft().type() : this.getParent().Define_TypeDecl_enclosingInstance(this, caller);
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      return caller == this.getRightNoTransform() ? this.getLeft().type().typeName() : this.getParent().Define_String_methodHost(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
