package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Scene;
import soot.SootFieldRef;
import soot.Type;
import soot.Value;

public class VarAccess extends Access implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected int isConstant_visited;
   protected boolean isConstant_computed;
   protected boolean isConstant_initialized;
   protected boolean isConstant_value;
   protected Map isDAafter_Variable_values;
   protected boolean decls_computed;
   protected SimpleSet decls_value;
   protected boolean decl_computed;
   protected Variable decl_value;
   protected boolean isFieldAccess_computed;
   protected boolean isFieldAccess_value;
   protected boolean type_computed;
   protected TypeDecl type_value;
   protected Map base_Body_values;

   public void flushCache() {
      super.flushCache();
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
      this.isDAafter_Variable_values = null;
      this.decls_computed = false;
      this.decls_value = null;
      this.decl_computed = false;
      this.decl_value = null;
      this.isFieldAccess_computed = false;
      this.type_computed = false;
      this.type_value = null;
      this.base_Body_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VarAccess clone() throws CloneNotSupportedException {
      VarAccess node = (VarAccess)super.clone();
      node.isConstant_visited = -1;
      node.isConstant_computed = false;
      node.isConstant_initialized = false;
      node.isDAafter_Variable_values = null;
      node.decls_computed = false;
      node.decls_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.isFieldAccess_computed = false;
      node.type_computed = false;
      node.type_value = null;
      node.base_Body_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VarAccess copy() {
      try {
         VarAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VarAccess fullCopy() {
      VarAccess tree = this.copy();
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

   public void definiteAssignment() {
      if (this.isSource()) {
         if (this.decl() instanceof VariableDeclaration) {
            VariableDeclaration v = (VariableDeclaration)this.decl();
            if (!v.isValue()) {
               if (v.isBlankFinal()) {
                  if (!this.isDAbefore(v)) {
                     this.error("Final variable " + v.name() + " is not assigned before used");
                  }
               } else if (!this.isDAbefore(v)) {
                  this.error("Local variable " + v.name() + " is not assigned before used");
               }
            }
         } else if (this.decl() instanceof FieldDeclaration && !this.isQualified()) {
            FieldDeclaration f = (FieldDeclaration)this.decl();
            if (f.isFinal() && !f.hasInit() && !this.isDAbefore(f)) {
               this.error("Final field " + f + " is not assigned before used");
            }
         }
      }

      if (this.isDest()) {
         Variable v = this.decl();
         if (v.isFinal() && v.isBlank() && !this.hostType().instanceOf(v.hostType())) {
            this.error("The final variable is not a blank final in this context, so it may not be assigned.");
         } else if (!v.isFinal() || !this.isQualified() || this.qualifier().isThisAccess() && !((Access)this.qualifier()).isQualified()) {
            if (v instanceof VariableDeclaration) {
               VariableDeclaration var = (VariableDeclaration)v;
               if (!var.isValue() && var.getParent().getParent().getParent() instanceof SwitchStmt && var.isFinal()) {
                  if (!this.isDUbefore(var)) {
                     this.error("Final variable " + var.name() + " may only be assigned once");
                  }
               } else if (var.isValue()) {
                  if (var.hasInit() || !this.isDUbefore(var)) {
                     this.error("Final variable " + var.name() + " may only be assigned once");
                  }
               } else if (var.isBlankFinal() && (var.hasInit() || !this.isDUbefore(var))) {
                  this.error("Final variable " + var.name() + " may only be assigned once");
               }

               if (var.isFinal() && !var.hasInit() && !this.isDUbefore(var)) {
               }
            } else if (v instanceof FieldDeclaration) {
               FieldDeclaration f = (FieldDeclaration)v;
               if (f.isFinal()) {
                  if (f.hasInit()) {
                     this.error("initialized field " + f.name() + " can not be assigned");
                  } else {
                     BodyDecl bodyDecl = this.enclosingBodyDecl();
                     if (!(bodyDecl instanceof ConstructorDecl) && !(bodyDecl instanceof InstanceInitializer) && !(bodyDecl instanceof StaticInitializer) && !(bodyDecl instanceof FieldDeclaration)) {
                        this.error("final field " + f.name() + " may only be assigned in constructors and initializers");
                     } else if (!this.isDUbefore(f)) {
                        this.error("Final field " + f.name() + " may only be assigned once");
                     }
                  }
               }
            } else if (v.isParameter() && v.isFinal()) {
               this.error("Final parameter " + v.name() + " may not be assigned");
            }
         } else {
            this.error("the blank final field " + v.name() + " may only be assigned by simple name");
         }
      }

   }

   protected boolean checkDUeverywhere(Variable v) {
      return this.isDest() && this.decl() == v ? false : super.checkDUeverywhere(v);
   }

   public void nameCheck() {
      if (this.decls().isEmpty() && (!this.isQualified() || !this.qualifier().type().isUnknown() || this.qualifier().isPackageAccess())) {
         this.error("no field named " + this.name() + " is accessible");
      }

      if (this.decls().size() > 1) {
         StringBuffer s = new StringBuffer();
         s.append("several fields named " + this.name());
         Iterator iter = this.decls().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            s.append("\n    " + v.type().typeName() + "." + v.name() + " declared in " + v.hostType().typeName());
         }

         this.error(s.toString());
      }

      if (this.inExplicitConstructorInvocation() && !this.isQualified() && this.decl().isInstanceVariable() && this.hostType() == this.decl().hostType()) {
         this.error("instance variable " + this.name() + " may not be accessed in an explicit constructor invocation");
      }

      Variable v = this.decl();
      if (!v.isFinal() && !v.isClassVariable() && !v.isInstanceVariable() && v.hostType() != this.hostType()) {
         this.error("A parameter/variable used but not declared in an inner class must be declared final");
      }

      if ((this.decl().isInstanceVariable() || this.decl().isClassVariable()) && !this.isQualified() && this.hostType() != null && !this.hostType().declaredBeforeUse(this.decl(), this) && this.inSameInitializer() && !this.simpleAssignment() && this.inDeclaringClass()) {
         BodyDecl b = this.closestBodyDecl(this.hostType());
         this.error("variable " + this.decl().name() + " is used in " + b + " before it is declared");
      }

   }

   public BodyDecl closestBodyDecl(TypeDecl t) {
      Object node;
      for(node = this; !(((ASTNode)node).getParent().getParent() instanceof Program) && ((ASTNode)node).getParent().getParent() != t; node = ((ASTNode)node).getParent()) {
      }

      return node instanceof BodyDecl ? (BodyDecl)node : null;
   }

   public VarAccess(String name, int start, int end) {
      this(name);
      this.start = this.IDstart = start;
      this.end = this.IDend = end;
   }

   public void toString(StringBuffer s) {
      s.append(this.name());
   }

   public void checkModifiers() {
      if (this.decl() instanceof FieldDeclaration) {
         FieldDeclaration f = (FieldDeclaration)this.decl();
         if (f.isDeprecated() && !this.withinDeprecatedAnnotation() && this.hostType().topLevelType() != f.hostType().topLevelType() && !this.withinSuppressWarnings("deprecation")) {
            this.warning(f.name() + " in " + f.hostType().typeName() + " has been deprecated");
         }
      }

   }

   protected void checkEnum(EnumDecl enumDecl) {
      super.checkEnum(enumDecl);
      if (this.decl().isStatic() && this.decl().hostType() == enumDecl && !this.isConstant()) {
         this.error("may not reference a static field of an enum type from here");
      }

   }

   private TypeDecl refined_InnerClasses_VarAccess_fieldQualifierType() {
      if (this.hasPrevExpr()) {
         return this.prevExpr().type();
      } else {
         TypeDecl typeDecl;
         for(typeDecl = this.hostType(); typeDecl != null && !typeDecl.hasField(this.name()); typeDecl = typeDecl.enclosingType()) {
         }

         return typeDecl != null ? typeDecl : this.decl().hostType();
      }
   }

   public void collectEnclosingVariables(HashSet set, TypeDecl typeDecl) {
      Variable v = this.decl();
      if (!v.isInstanceVariable() && !v.isClassVariable() && v.hostType() == typeDecl) {
         set.add(v);
      }

      super.collectEnclosingVariables(set, typeDecl);
   }

   public void transformation() {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = (FieldDeclaration)v;
         if (this.requiresAccessor()) {
            TypeDecl typeDecl = this.fieldQualifierType();
            if (this.isSource()) {
               f.createAccessor(typeDecl);
            }

            if (this.isDest()) {
               f.createAccessorWrite(typeDecl);
            }
         }
      }

      super.transformation();
   }

   public Value refined_Expressions_VarAccess_eval(Body b) {
      Variable v = this.decl();
      if (v instanceof VariableDeclaration) {
         VariableDeclaration decl = (VariableDeclaration)v;
         return (Value)(decl.hostType() == this.hostType() ? decl.local : this.emitLoadLocalInNestedClass(b, decl));
      } else if (v instanceof ParameterDeclaration) {
         ParameterDeclaration decl = (ParameterDeclaration)v;
         return (Value)(decl.hostType() == this.hostType() ? decl.local : this.emitLoadLocalInNestedClass(b, decl));
      } else if (v instanceof FieldDeclaration) {
         FieldDeclaration f = (FieldDeclaration)v;
         if (f.hostType().isArrayDecl() && f.name().equals("length")) {
            return b.newLengthExpr(this.asImmediate(b, this.createLoadQualifier(b)), this);
         } else if (f.isStatic()) {
            if (this.isQualified() && !this.qualifier().isTypeAccess()) {
               b.newTemp(this.qualifier().eval(b));
            }

            if (this.requiresAccessor()) {
               ArrayList list = new ArrayList();
               return b.newStaticInvokeExpr(f.createAccessor(this.fieldQualifierType()).sootRef(), (java.util.List)list, this);
            } else {
               return b.newStaticFieldRef(this.sootRef(), this);
            }
         } else {
            Local base;
            if (this.requiresAccessor()) {
               base = this.base(b);
               ArrayList list = new ArrayList();
               list.add(base);
               return b.newStaticInvokeExpr(f.createAccessor(this.fieldQualifierType()).sootRef(), (java.util.List)list, this);
            } else {
               base = this.createLoadQualifier(b);
               return b.newInstanceFieldRef(base, this.sootRef(), this);
            }
         }
      } else {
         return super.eval(b);
      }
   }

   public Value refined_Expressions_VarAccess_emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = (FieldDeclaration)v;
         if (this.requiresAccessor()) {
            if (f.isStatic()) {
               ArrayList list = new ArrayList();
               list.add(rvalue);
               return this.asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(this.fieldQualifierType()).sootRef(), (java.util.List)list, location));
            }

            Local base = this.base(b);
            ArrayList list = new ArrayList();
            list.add(base);
            list.add(this.asLocal(b, rvalue, lvalue.getType()));
            return this.asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(this.fieldQualifierType()).sootRef(), (java.util.List)list, location));
         }
      }

      return super.emitStore(b, lvalue, rvalue, location);
   }

   public void collectTypesToSignatures(Collection<Type> set) {
      super.collectTypesToSignatures(set);
      if (this.decl() instanceof FieldDeclaration) {
         this.addDependencyIfNeeded(set, this.fieldQualifierType());
      }

   }

   public VarAccess() {
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.isFieldAccess_computed = false;
      this.type_computed = false;
   }

   public void init$Children() {
   }

   public VarAccess(String p0) {
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.isFieldAccess_computed = false;
      this.type_computed = false;
      this.setID(p0);
   }

   public VarAccess(Symbol p0) {
      this.isConstant_visited = -1;
      this.isConstant_computed = false;
      this.isConstant_initialized = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.isFieldAccess_computed = false;
      this.type_computed = false;
      this.setID(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setID(String value) {
      this.tokenString_ID = value;
   }

   public void setID(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setID is only valid for String lexemes");
      } else {
         this.tokenString_ID = (String)symbol.value;
         this.IDstart = symbol.getStart();
         this.IDend = symbol.getEnd();
      }
   }

   public String getID() {
      return this.tokenString_ID != null ? this.tokenString_ID : "";
   }

   protected TypeDecl refined_GenericsCodegen_VarAccess_fieldQualifierType() {
      TypeDecl typeDecl = this.refined_InnerClasses_VarAccess_fieldQualifierType();
      return typeDecl == null ? null : typeDecl.erasure();
   }

   public Value eval(Body b) {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = ((FieldDeclaration)v).erasedField();
         if (f.hostType().isArrayDecl() && f.name().equals("length")) {
            return b.newLengthExpr(this.asImmediate(b, this.createLoadQualifier(b)), this);
         } else {
            Object result;
            if (f.isStatic()) {
               if (this.isQualified() && !this.qualifier().isTypeAccess()) {
                  b.newTemp(this.qualifier().eval(b));
               }

               if (this.requiresAccessor()) {
                  ArrayList list = new ArrayList();
                  result = b.newStaticInvokeExpr(f.createAccessor(this.fieldQualifierType().erasure()).sootRef(), (java.util.List)list, this);
               } else {
                  result = b.newStaticFieldRef(this.sootRef(), this);
               }
            } else {
               Local base;
               if (this.requiresAccessor()) {
                  base = this.base(b);
                  ArrayList list = new ArrayList();
                  list.add(base);
                  result = b.newStaticInvokeExpr(f.createAccessor(this.fieldQualifierType().erasure()).sootRef(), (java.util.List)list, this);
               } else {
                  base = this.createLoadQualifier(b);
                  result = b.newInstanceFieldRef(base, this.sootRef(), this);
               }
            }

            if (f.type() != v.type()) {
               result = f.type().emitCastTo(b, (Value)result, v.type(), this);
            }

            return (Value)result;
         }
      } else {
         return this.refined_Expressions_VarAccess_eval(b);
      }
   }

   private SootFieldRef sootRef() {
      FieldDeclaration decl = ((FieldDeclaration)this.decl()).erasedField();
      SootFieldRef ref = Scene.v().makeFieldRef(this.fieldQualifierType().getSootClassDecl(), decl.name(), decl.type().getSootType(), decl.isStatic());
      return ref;
   }

   public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = ((FieldDeclaration)v).erasedField();
         if (this.requiresAccessor()) {
            if (f.isStatic()) {
               ArrayList list = new ArrayList();
               list.add(rvalue);
               return this.asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(this.fieldQualifierType().erasure()).sootRef(), (java.util.List)list, this));
            }

            Local base = this.base(b);
            ArrayList list = new ArrayList();
            list.add(base);
            list.add(this.asLocal(b, rvalue, lvalue.getType()));
            return this.asLocal(b, b.newStaticInvokeExpr(f.createAccessorWrite(this.fieldQualifierType().erasure()).sootRef(), (java.util.List)list, this));
         }
      }

      return this.refined_Expressions_VarAccess_emitStore(b, lvalue, rvalue, location);
   }

   public Local createLoadQualifier(Body b) {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = ((FieldDeclaration)v).erasedField();
         if (this.hasPrevExpr()) {
            Local qualifier = this.asLocal(b, this.prevExpr().eval(b));
            return qualifier;
         }

         if (f.isInstanceVariable()) {
            return this.emitThis(b, this.fieldQualifierType().erasure());
         }
      }

      throw new Error("createLoadQualifier not supported for " + v.getClass().getName());
   }

   protected TypeDecl fieldQualifierType() {
      TypeDecl typeDecl = this.refined_GenericsCodegen_VarAccess_fieldQualifierType();
      return typeDecl != null ? typeDecl : this.decl().hostType();
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      return this.type().cast(this.decl().getInit().constant());
   }

   public boolean isConstant() {
      if (this.isConstant_computed) {
         return this.isConstant_value;
      } else {
         ASTNode$State state = this.state();
         if (!this.isConstant_initialized) {
            this.isConstant_initialized = true;
            this.isConstant_value = false;
         }

         if (state.IN_CIRCLE) {
            if (this.isConstant_visited != state.CIRCLE_INDEX) {
               this.isConstant_visited = state.CIRCLE_INDEX;
               if (state.RESET_CYCLE) {
                  this.isConstant_computed = false;
                  this.isConstant_initialized = false;
                  this.isConstant_visited = -1;
                  return this.isConstant_value;
               } else {
                  boolean new_isConstant_value = this.isConstant_compute();
                  if (new_isConstant_value != this.isConstant_value) {
                     state.CHANGE = true;
                  }

                  this.isConstant_value = new_isConstant_value;
                  return this.isConstant_value;
               }
            } else {
               return this.isConstant_value;
            }
         } else {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = this.is$Final();

            do {
               this.isConstant_visited = state.CIRCLE_INDEX;
               state.CHANGE = false;
               boolean new_isConstant_value = this.isConstant_compute();
               if (new_isConstant_value != this.isConstant_value) {
                  state.CHANGE = true;
               }

               this.isConstant_value = new_isConstant_value;
               ++state.CIRCLE_INDEX;
            } while(state.CHANGE);

            if (isFinal && num == this.state().boundariesCrossed) {
               this.isConstant_computed = true;
            } else {
               state.RESET_CYCLE = true;
               this.isConstant_compute();
               state.RESET_CYCLE = false;
               this.isConstant_computed = false;
               this.isConstant_initialized = false;
            }

            state.IN_CIRCLE = false;
            return this.isConstant_value;
         }
      }
   }

   private boolean isConstant_compute() {
      Variable v = this.decl();
      if (v instanceof FieldDeclaration) {
         FieldDeclaration f = (FieldDeclaration)v;
         return f.isConstant() && (!this.isQualified() || this.isQualified() && this.qualifier().isTypeAccess());
      } else {
         boolean result = v.isFinal() && v.hasInit() && v.getInit().isConstant() && (v.type().isPrimitive() || v.type().isString());
         return result && (!this.isQualified() || this.isQualified() && this.qualifier().isTypeAccess());
      }
   }

   public Variable varDecl() {
      ASTNode$State state = this.state();
      return this.decl();
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
      return this.isDAbefore(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUbefore(v);
   }

   public boolean unassignedEverywhere(Variable v, TryStmt stmt) {
      ASTNode$State state = this.state();
      return this.isDest() && this.decl() == v && this.enclosingStmt().reachable() ? false : super.unassignedEverywhere(v, stmt);
   }

   public SimpleSet decls() {
      if (this.decls_computed) {
         return this.decls_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decls_value = this.decls_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decls_computed = true;
         }

         return this.decls_value;
      }
   }

   private SimpleSet decls_compute() {
      SimpleSet set = this.lookupVariable(this.name());
      if (set.size() == 1) {
         Variable v = (Variable)set.iterator().next();
         if (!this.isQualified() && this.inStaticContext()) {
            if (v.isInstanceVariable() && !this.hostType().memberFields(v.name()).isEmpty()) {
               return SimpleSet.emptySet;
            }
         } else if (this.isQualified() && this.qualifier().staticContextQualifier() && v.isInstanceVariable()) {
            return SimpleSet.emptySet;
         }
      }

      return set;
   }

   public Variable decl() {
      if (this.decl_computed) {
         return this.decl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.decl_value = this.decl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.decl_computed = true;
         }

         return this.decl_value;
      }
   }

   private Variable decl_compute() {
      SimpleSet decls = this.decls();
      return decls.size() == 1 ? (Variable)decls.iterator().next() : this.unknownField();
   }

   public boolean inSameInitializer() {
      ASTNode$State state = this.state();
      BodyDecl b = this.closestBodyDecl(this.decl().hostType());
      if (b == null) {
         return false;
      } else if (b instanceof FieldDeclaration && ((FieldDeclaration)b).isStatic() == this.decl().isStatic()) {
         return true;
      } else if (b instanceof InstanceInitializer && !this.decl().isStatic()) {
         return true;
      } else {
         return b instanceof StaticInitializer && this.decl().isStatic();
      }
   }

   public boolean simpleAssignment() {
      ASTNode$State state = this.state();
      return this.isDest() && this.getParent() instanceof AssignSimpleExpr;
   }

   public boolean inDeclaringClass() {
      ASTNode$State state = this.state();
      return this.hostType() == this.decl().hostType();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public boolean isFieldAccess() {
      if (this.isFieldAccess_computed) {
         return this.isFieldAccess_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isFieldAccess_value = this.isFieldAccess_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isFieldAccess_computed = true;
         }

         return this.isFieldAccess_value;
      }
   }

   private boolean isFieldAccess_compute() {
      return this.decl().isClassVariable() || this.decl().isInstanceVariable();
   }

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.AMBIGUOUS_NAME;
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
      return this.decl().type();
   }

   public boolean isVariable() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isEnumConstant() {
      ASTNode$State state = this.state();
      return this.varDecl() instanceof EnumConstant;
   }

   public boolean requiresAccessor() {
      ASTNode$State state = this.state();
      Variable v = this.decl();
      if (!(v instanceof FieldDeclaration)) {
         return false;
      } else {
         FieldDeclaration f = (FieldDeclaration)v;
         if (f.isPrivate() && !this.hostType().hasField(v.name())) {
            return true;
         } else {
            return f.isProtected() && !f.hostPackage().equals(this.hostPackage()) && !this.hostType().hasField(v.name());
         }
      }
   }

   public Local base(Body b) {
      if (this.base_Body_values == null) {
         this.base_Body_values = new HashMap(4);
      }

      if (this.base_Body_values.containsKey(b)) {
         return (Local)this.base_Body_values.get(b);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         Local base_Body_value = this.base_compute(b);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.base_Body_values.put(b, base_Body_value);
         }

         return base_Body_value;
      }
   }

   private Local base_compute(Body b) {
      return this.asLocal(b, this.createLoadQualifier(b));
   }

   public Collection<TypeDecl> throwTypes() {
      ASTNode$State state = this.state();
      return this.decl().throwTypes();
   }

   public boolean isVariable(Variable var) {
      ASTNode$State state = this.state();
      return this.decl() == var;
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
