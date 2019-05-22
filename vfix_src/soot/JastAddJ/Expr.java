package soot.JastAddJ;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import soot.Local;
import soot.Type;
import soot.Value;

public abstract class Expr extends ASTNode<ASTNode> implements Cloneable {
   protected boolean false_label_computed = false;
   protected soot.jimple.Stmt false_label_value;
   protected boolean true_label_computed = false;
   protected soot.jimple.Stmt true_label_value;

   public void flushCache() {
      super.flushCache();
      this.false_label_computed = false;
      this.false_label_value = null;
      this.true_label_computed = false;
      this.true_label_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Expr clone() throws CloneNotSupportedException {
      Expr node = (Expr)super.clone();
      node.false_label_computed = false;
      node.false_label_value = null;
      node.true_label_computed = false;
      node.true_label_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public SimpleSet keepAccessibleTypes(SimpleSet oldSet) {
      SimpleSet newSet = SimpleSet.emptySet;
      TypeDecl hostType = this.hostType();
      Iterator iter = oldSet.iterator();

      while(true) {
         TypeDecl t;
         do {
            if (!iter.hasNext()) {
               return newSet;
            }

            t = (TypeDecl)iter.next();
         } while((hostType == null || !t.accessibleFrom(hostType)) && (hostType != null || !t.accessibleFromPackage(this.hostPackage())));

         newSet = newSet.add(t);
      }
   }

   public SimpleSet keepAccessibleFields(SimpleSet oldSet) {
      SimpleSet newSet = SimpleSet.emptySet;
      Iterator iter = oldSet.iterator();

      while(iter.hasNext()) {
         Variable v = (Variable)iter.next();
         if (v instanceof FieldDeclaration) {
            FieldDeclaration f = (FieldDeclaration)v;
            if (this.mayAccess(f)) {
               newSet = newSet.add(f);
            }
         }
      }

      return newSet;
   }

   public boolean mayAccess(FieldDeclaration f) {
      if (f.isPublic()) {
         return true;
      } else if (f.isProtected()) {
         return f.hostPackage().equals(this.hostPackage()) ? true : this.hostType().mayAccess(this, f);
      } else if (f.isPrivate()) {
         return f.hostType().topLevelType() == this.hostType().topLevelType();
      } else {
         return f.hostPackage().equals(this.hostType().hostPackage());
      }
   }

   public Dot qualifiesAccess(Access access) {
      Dot dot = new Dot(this, access);
      dot.setStart(this.getStart());
      dot.setEnd(access.getEnd());
      return dot;
   }

   protected SimpleSet chooseConstructor(Collection constructors, List argList) {
      SimpleSet potentiallyApplicable = SimpleSet.emptySet;
      Iterator iter = constructors.iterator();

      while(iter.hasNext()) {
         ConstructorDecl decl = (ConstructorDecl)iter.next();
         if (decl.potentiallyApplicable(argList) && decl.accessibleFrom(this.hostType())) {
            potentiallyApplicable = potentiallyApplicable.add(decl);
         }
      }

      SimpleSet maxSpecific = SimpleSet.emptySet;
      Iterator iter = potentiallyApplicable.iterator();

      ConstructorDecl decl;
      while(iter.hasNext()) {
         decl = (ConstructorDecl)iter.next();
         if (decl.applicableBySubtyping(argList)) {
            maxSpecific = mostSpecific(maxSpecific, decl);
         }
      }

      if (maxSpecific.isEmpty()) {
         iter = potentiallyApplicable.iterator();

         while(iter.hasNext()) {
            decl = (ConstructorDecl)iter.next();
            if (decl.applicableByMethodInvocationConversion(argList)) {
               maxSpecific = mostSpecific(maxSpecific, decl);
            }
         }
      }

      if (maxSpecific.isEmpty()) {
         iter = potentiallyApplicable.iterator();

         while(iter.hasNext()) {
            decl = (ConstructorDecl)iter.next();
            if (decl.isVariableArity() && decl.applicableVariableArity(argList)) {
               maxSpecific = mostSpecific(maxSpecific, decl);
            }
         }
      }

      return maxSpecific;
   }

   protected static SimpleSet mostSpecific(SimpleSet maxSpecific, ConstructorDecl decl) {
      if (maxSpecific.isEmpty()) {
         maxSpecific = maxSpecific.add(decl);
      } else if (decl.moreSpecificThan((ConstructorDecl)maxSpecific.iterator().next())) {
         maxSpecific = SimpleSet.emptySet.add(decl);
      } else if (!((ConstructorDecl)maxSpecific.iterator().next()).moreSpecificThan(decl)) {
         maxSpecific = maxSpecific.add(decl);
      }

      return maxSpecific;
   }

   protected Value emitBooleanCondition(Body b) {
      b.setLine(this);
      this.emitEvalBranch(b);
      soot.jimple.Stmt end_label = this.newLabel();
      b.addLabel(this.false_label());
      Local result = b.newTemp((Type)soot.BooleanType.v());
      b.add(b.newAssignStmt(result, BooleanType.emitConstant(false), this));
      b.add(b.newGotoStmt(end_label, this));
      b.addLabel(this.true_label());
      b.add(b.newAssignStmt(result, BooleanType.emitConstant(true), this));
      b.addLabel(end_label);
      return result;
   }

   public void refined_BooleanExpressions_Expr_emitEvalBranch(Body b) {
      b.setLine(this);
      if (this.isTrue()) {
         b.add(b.newGotoStmt(this.true_label(), this));
      } else if (this.isFalse()) {
         b.add(b.newGotoStmt(this.false_label(), this));
      } else {
         b.add(b.newIfStmt(b.newEqExpr(this.asImmediate(b, this.eval(b)), BooleanType.emitConstant(false), this), this.false_label(), this));
         b.add(b.newGotoStmt(this.true_label(), this));
      }

   }

   public Value eval(Body b) {
      throw new Error("Operation eval not supported for " + this.getClass().getName());
   }

   public Value emitStore(Body b, Value lvalue, Value rvalue, ASTNode location) {
      b.setLine(this);
      b.add(b.newAssignStmt(lvalue, this.asLocal(b, rvalue, lvalue.getType()), location));
      return rvalue;
   }

   public void collectTypesToHierarchy(Collection<Type> set) {
      super.collectTypesToHierarchy(set);
      this.addDependencyIfNeeded(set, this.type());
   }

   protected void addDependencyIfNeeded(Collection<Type> set, TypeDecl type) {
      type = type.elementType().erasure();
      if (type.isReferenceType() && !type.isUnknown()) {
         set.add(type.getSootType());
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

   public void emitEvalBranch(Body b) {
      if (this.type().isReferenceType()) {
         b.setLine(this);
         b.add(b.newIfStmt(b.newEqExpr(this.asImmediate(b, this.type().emitUnboxingOperation(b, this.eval(b), this)), BooleanType.emitConstant(false), this), this.false_label(), this));
         b.add(b.newGotoStmt(this.true_label(), this));
      } else {
         this.refined_BooleanExpressions_Expr_emitEvalBranch(b);
      }

   }

   public abstract TypeDecl type();

   public Constant constant() {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException("ConstantExpression operation constant not supported for type " + this.getClass().getName());
   }

   public boolean isPositive() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean representableIn(TypeDecl t) {
      ASTNode$State state = this.state();
      if (!this.type().isByte() && !this.type().isChar() && !this.type().isShort() && !this.type().isInt()) {
         return false;
      } else if (t.isByte()) {
         return this.constant().intValue() >= -128 && this.constant().intValue() <= 127;
      } else if (t.isChar()) {
         return this.constant().intValue() >= 0 && this.constant().intValue() <= 65535;
      } else if (t.isShort()) {
         return this.constant().intValue() >= -32768 && this.constant().intValue() <= 32767;
      } else if (!t.isInt()) {
         return false;
      } else {
         return this.constant().intValue() >= Integer.MIN_VALUE && this.constant().intValue() <= Integer.MAX_VALUE;
      }
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isTrue() {
      ASTNode$State state = this.state();
      return this.isConstant() && this.type() instanceof BooleanType && this.constant().booleanValue();
   }

   public boolean isFalse() {
      ASTNode$State state = this.state();
      return this.isConstant() && this.type() instanceof BooleanType && !this.constant().booleanValue();
   }

   public Variable varDecl() {
      ASTNode$State state = this.state();
      return null;
   }

   public boolean isDAafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isTrue() || this.isDAbefore(v);
   }

   public boolean isDAafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isFalse() || this.isDAbefore(v);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafterFalse(v) && this.isDAafterTrue(v) || this.isDAbefore(v);
   }

   public boolean isDUafterFalse(Variable v) {
      ASTNode$State state = this.state();
      return this.isTrue() ? true : this.isDUbefore(v);
   }

   public boolean isDUafterTrue(Variable v) {
      ASTNode$State state = this.state();
      return this.isFalse() ? true : this.isDUbefore(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafterFalse(v) && this.isDUafterTrue(v) || this.isDUbefore(v);
   }

   public SimpleSet mostSpecificConstructor(Collection constructors) {
      ASTNode$State state = this.state();
      SimpleSet maxSpecific = SimpleSet.emptySet;
      Iterator iter = constructors.iterator();

      while(iter.hasNext()) {
         ConstructorDecl decl = (ConstructorDecl)iter.next();
         if (this.applicableAndAccessible(decl)) {
            if (maxSpecific.isEmpty()) {
               maxSpecific = maxSpecific.add(decl);
            } else if (decl.moreSpecificThan((ConstructorDecl)maxSpecific.iterator().next())) {
               maxSpecific = SimpleSet.emptySet.add(decl);
            } else if (!((ConstructorDecl)maxSpecific.iterator().next()).moreSpecificThan(decl)) {
               maxSpecific = maxSpecific.add(decl);
            }
         }
      }

      return maxSpecific;
   }

   public boolean applicableAndAccessible(ConstructorDecl decl) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean hasQualifiedPackage(String packageName) {
      ASTNode$State state = this.state();
      return false;
   }

   public SimpleSet qualifiedLookupType(String name) {
      ASTNode$State state = this.state();
      return this.keepAccessibleTypes(this.type().memberTypes(name));
   }

   public SimpleSet qualifiedLookupVariable(String name) {
      ASTNode$State state = this.state();
      return this.type().accessibleFrom(this.hostType()) ? this.keepAccessibleFields(this.type().memberFields(name)) : SimpleSet.emptySet;
   }

   public String packageName() {
      ASTNode$State state = this.state();
      return "";
   }

   public String typeName() {
      ASTNode$State state = this.state();
      return "";
   }

   public boolean isTypeAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isMethodAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isFieldAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isSuperAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isThisAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isPackageAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isArrayAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isClassAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isSuperConstructorAccess() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isLeftChildOfDot() {
      ASTNode$State state = this.state();
      return this.hasParentDot() && this.parentDot().getLeft() == this;
   }

   public boolean isRightChildOfDot() {
      ASTNode$State state = this.state();
      return this.hasParentDot() && this.parentDot().getRight() == this;
   }

   public AbstractDot parentDot() {
      ASTNode$State state = this.state();
      return this.getParent() instanceof AbstractDot ? (AbstractDot)this.getParent() : null;
   }

   public boolean hasParentDot() {
      ASTNode$State state = this.state();
      return this.parentDot() != null;
   }

   public Access nextAccess() {
      ASTNode$State state = this.state();
      return this.parentDot().nextAccess();
   }

   public boolean hasNextAccess() {
      ASTNode$State state = this.state();
      return this.isLeftChildOfDot();
   }

   public Stmt enclosingStmt() {
      ASTNode$State state = this.state();

      Object node;
      for(node = this; node != null && !(node instanceof Stmt); node = ((ASTNode)node).getParent()) {
      }

      return (Stmt)node;
   }

   public boolean isVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isUnknown() {
      ASTNode$State state = this.state();
      return this.type().isUnknown();
   }

   public boolean staticContextQualifier() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isEnumConstant() {
      ASTNode$State state = this.state();
      return false;
   }

   public soot.jimple.Stmt false_label() {
      if (this.false_label_computed) {
         return this.false_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.false_label_value = this.false_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.false_label_computed = true;
         }

         return this.false_label_value;
      }
   }

   private soot.jimple.Stmt false_label_compute() {
      return this.getParent().definesLabel() ? this.condition_false_label() : this.newLabel();
   }

   public soot.jimple.Stmt true_label() {
      if (this.true_label_computed) {
         return this.true_label_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.true_label_value = this.true_label_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.true_label_computed = true;
         }

         return this.true_label_value;
      }
   }

   private soot.jimple.Stmt true_label_compute() {
      return this.getParent().definesLabel() ? this.condition_true_label() : this.newLabel();
   }

   public boolean canBeTrue() {
      ASTNode$State state = this.state();
      return !this.isFalse();
   }

   public boolean canBeFalse() {
      ASTNode$State state = this.state();
      return !this.isTrue();
   }

   public Collection<TypeDecl> throwTypes() {
      ASTNode$State state = this.state();
      Collection<TypeDecl> tts = new LinkedList();
      tts.add(this.type());
      return tts;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isVariable(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isDest() {
      ASTNode$State state = this.state();
      boolean isDest_value = this.getParent().Define_boolean_isDest(this, (ASTNode)null);
      return isDest_value;
   }

   public boolean isSource() {
      ASTNode$State state = this.state();
      boolean isSource_value = this.getParent().Define_boolean_isSource(this, (ASTNode)null);
      return isSource_value;
   }

   public boolean isIncOrDec() {
      ASTNode$State state = this.state();
      boolean isIncOrDec_value = this.getParent().Define_boolean_isIncOrDec(this, (ASTNode)null);
      return isIncOrDec_value;
   }

   public boolean isDAbefore(Variable v) {
      ASTNode$State state = this.state();
      boolean isDAbefore_Variable_value = this.getParent().Define_boolean_isDAbefore(this, (ASTNode)null, v);
      return isDAbefore_Variable_value;
   }

   public boolean isDUbefore(Variable v) {
      ASTNode$State state = this.state();
      boolean isDUbefore_Variable_value = this.getParent().Define_boolean_isDUbefore(this, (ASTNode)null, v);
      return isDUbefore_Variable_value;
   }

   public Collection lookupMethod(String name) {
      ASTNode$State state = this.state();
      Collection lookupMethod_String_value = this.getParent().Define_Collection_lookupMethod(this, (ASTNode)null, name);
      return lookupMethod_String_value;
   }

   public TypeDecl typeBoolean() {
      ASTNode$State state = this.state();
      TypeDecl typeBoolean_value = this.getParent().Define_TypeDecl_typeBoolean(this, (ASTNode)null);
      return typeBoolean_value;
   }

   public TypeDecl typeByte() {
      ASTNode$State state = this.state();
      TypeDecl typeByte_value = this.getParent().Define_TypeDecl_typeByte(this, (ASTNode)null);
      return typeByte_value;
   }

   public TypeDecl typeShort() {
      ASTNode$State state = this.state();
      TypeDecl typeShort_value = this.getParent().Define_TypeDecl_typeShort(this, (ASTNode)null);
      return typeShort_value;
   }

   public TypeDecl typeChar() {
      ASTNode$State state = this.state();
      TypeDecl typeChar_value = this.getParent().Define_TypeDecl_typeChar(this, (ASTNode)null);
      return typeChar_value;
   }

   public TypeDecl typeInt() {
      ASTNode$State state = this.state();
      TypeDecl typeInt_value = this.getParent().Define_TypeDecl_typeInt(this, (ASTNode)null);
      return typeInt_value;
   }

   public TypeDecl typeLong() {
      ASTNode$State state = this.state();
      TypeDecl typeLong_value = this.getParent().Define_TypeDecl_typeLong(this, (ASTNode)null);
      return typeLong_value;
   }

   public TypeDecl typeFloat() {
      ASTNode$State state = this.state();
      TypeDecl typeFloat_value = this.getParent().Define_TypeDecl_typeFloat(this, (ASTNode)null);
      return typeFloat_value;
   }

   public TypeDecl typeDouble() {
      ASTNode$State state = this.state();
      TypeDecl typeDouble_value = this.getParent().Define_TypeDecl_typeDouble(this, (ASTNode)null);
      return typeDouble_value;
   }

   public TypeDecl typeString() {
      ASTNode$State state = this.state();
      TypeDecl typeString_value = this.getParent().Define_TypeDecl_typeString(this, (ASTNode)null);
      return typeString_value;
   }

   public TypeDecl typeVoid() {
      ASTNode$State state = this.state();
      TypeDecl typeVoid_value = this.getParent().Define_TypeDecl_typeVoid(this, (ASTNode)null);
      return typeVoid_value;
   }

   public TypeDecl typeNull() {
      ASTNode$State state = this.state();
      TypeDecl typeNull_value = this.getParent().Define_TypeDecl_typeNull(this, (ASTNode)null);
      return typeNull_value;
   }

   public TypeDecl unknownType() {
      ASTNode$State state = this.state();
      TypeDecl unknownType_value = this.getParent().Define_TypeDecl_unknownType(this, (ASTNode)null);
      return unknownType_value;
   }

   public boolean hasPackage(String packageName) {
      ASTNode$State state = this.state();
      boolean hasPackage_String_value = this.getParent().Define_boolean_hasPackage(this, (ASTNode)null, packageName);
      return hasPackage_String_value;
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
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
   }

   public NameType nameType() {
      ASTNode$State state = this.state();
      NameType nameType_value = this.getParent().Define_NameType_nameType(this, (ASTNode)null);
      return nameType_value;
   }

   public BodyDecl enclosingBodyDecl() {
      ASTNode$State state = this.state();
      BodyDecl enclosingBodyDecl_value = this.getParent().Define_BodyDecl_enclosingBodyDecl(this, (ASTNode)null);
      return enclosingBodyDecl_value;
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

   public String methodHost() {
      ASTNode$State state = this.state();
      String methodHost_value = this.getParent().Define_String_methodHost(this, (ASTNode)null);
      return methodHost_value;
   }

   public boolean inStaticContext() {
      ASTNode$State state = this.state();
      boolean inStaticContext_value = this.getParent().Define_boolean_inStaticContext(this, (ASTNode)null);
      return inStaticContext_value;
   }

   public TypeDecl assignConvertedType() {
      ASTNode$State state = this.state();
      TypeDecl assignConvertedType_value = this.getParent().Define_TypeDecl_assignConvertedType(this, (ASTNode)null);
      return assignConvertedType_value;
   }

   public boolean inExtendsOrImplements() {
      ASTNode$State state = this.state();
      boolean inExtendsOrImplements_value = this.getParent().Define_boolean_inExtendsOrImplements(this, (ASTNode)null);
      return inExtendsOrImplements_value;
   }

   public soot.jimple.Stmt condition_false_label() {
      ASTNode$State state = this.state();
      soot.jimple.Stmt condition_false_label_value = this.getParent().Define_soot_jimple_Stmt_condition_false_label(this, (ASTNode)null);
      return condition_false_label_value;
   }

   public soot.jimple.Stmt condition_true_label() {
      ASTNode$State state = this.state();
      soot.jimple.Stmt condition_true_label_value = this.getParent().Define_soot_jimple_Stmt_condition_true_label(this, (ASTNode)null);
      return condition_true_label_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
