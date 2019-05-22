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
import soot.SootMethodRef;
import soot.Type;
import soot.Value;

public class MethodAccess extends Access implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map computeDAbefore_int_Variable_values;
   protected boolean exceptionCollection_computed;
   protected Collection exceptionCollection_value;
   protected boolean decls_computed;
   protected SimpleSet decls_value;
   protected boolean decl_computed;
   protected MethodDecl decl_value;
   protected boolean type_computed;
   protected TypeDecl type_value;
   protected Map typeArguments_MethodDecl_values;

   public void flushCache() {
      super.flushCache();
      this.computeDAbefore_int_Variable_values = null;
      this.exceptionCollection_computed = false;
      this.exceptionCollection_value = null;
      this.decls_computed = false;
      this.decls_value = null;
      this.decl_computed = false;
      this.decl_value = null;
      this.type_computed = false;
      this.type_value = null;
      this.typeArguments_MethodDecl_values = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public MethodAccess clone() throws CloneNotSupportedException {
      MethodAccess node = (MethodAccess)super.clone();
      node.computeDAbefore_int_Variable_values = null;
      node.exceptionCollection_computed = false;
      node.exceptionCollection_value = null;
      node.decls_computed = false;
      node.decls_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.type_computed = false;
      node.type_value = null;
      node.typeArguments_MethodDecl_values = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public MethodAccess copy() {
      try {
         MethodAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public MethodAccess fullCopy() {
      MethodAccess tree = this.copy();
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

      for(int i = 0; i < this.decl().getNumException(); ++i) {
         c.add(this.decl().getException(i).type());
      }

   }

   public void exceptionHandling() {
      Iterator iter = this.exceptionCollection().iterator();

      while(iter.hasNext()) {
         TypeDecl exceptionType = (TypeDecl)iter.next();
         if (!this.handlesException(exceptionType)) {
            this.error("" + this.decl().hostType().fullName() + "." + this + " invoked in " + this.hostType().fullName() + " may throw uncaught exception " + exceptionType.fullName());
         }
      }

   }

   protected boolean reachedException(TypeDecl catchType) {
      Iterator iter = this.exceptionCollection().iterator();

      TypeDecl exceptionType;
      do {
         if (!iter.hasNext()) {
            return super.reachedException(catchType);
         }

         exceptionType = (TypeDecl)iter.next();
      } while(!catchType.mayCatch(exceptionType));

      return true;
   }

   private static SimpleSet removeInstanceMethods(SimpleSet c) {
      SimpleSet set = SimpleSet.emptySet;
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         if (m.isStatic()) {
            set = set.add(m);
         }
      }

      return set;
   }

   public boolean applicable(MethodDecl decl) {
      if (this.getNumArg() != decl.getNumParameter()) {
         return false;
      } else if (!this.name().equals(decl.name())) {
         return false;
      } else {
         for(int i = 0; i < this.getNumArg(); ++i) {
            if (!this.getArg(i).type().instanceOf(decl.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public MethodAccess(String name, List args, int start, int end) {
      this(name, args);
      this.setStart(start);
      this.setEnd(end);
   }

   public void toString(StringBuffer s) {
      s.append(this.name());
      s.append("(");
      if (this.getNumArg() > 0) {
         this.getArg(0).toString(s);

         for(int i = 1; i < this.getNumArg(); ++i) {
            s.append(", ");
            this.getArg(i).toString(s);
         }
      }

      s.append(")");
   }

   public void nameCheck() {
      if (this.isQualified() && this.qualifier().isPackageAccess() && !this.qualifier().isUnknown()) {
         this.error("The method " + this.decl().signature() + " can not be qualified by a package name.");
      }

      if (this.isQualified() && this.decl().isAbstract() && this.qualifier().isSuperAccess()) {
         this.error("may not access abstract methods in superclass");
      }

      if (this.decls().isEmpty() && (!this.isQualified() || !this.qualifier().isUnknown())) {
         StringBuffer s = new StringBuffer();
         s.append("no method named " + this.name());
         s.append("(");

         for(int i = 0; i < this.getNumArg(); ++i) {
            if (i != 0) {
               s.append(", ");
            }

            s.append(this.getArg(i).type().typeName());
         }

         s.append(") in " + this.methodHost() + " matches.");
         if (this.singleCandidateDecl() != null) {
            s.append(" However, there is a method " + this.singleCandidateDecl().signature());
         }

         this.error(s.toString());
      }

      if (this.decls().size() > 1) {
         boolean allAbstract = true;
         Iterator iter = this.decls().iterator();

         while(iter.hasNext() && allAbstract) {
            MethodDecl m = (MethodDecl)iter.next();
            if (!m.isAbstract() && !m.hostType().isObject()) {
               allAbstract = false;
            }
         }

         if (!allAbstract && this.validArgs()) {
            StringBuffer s = new StringBuffer();
            s.append("several most specific methods for " + this + "\n");
            Iterator iter = this.decls().iterator();

            while(iter.hasNext()) {
               MethodDecl m = (MethodDecl)iter.next();
               s.append("    " + m.signature() + " in " + m.hostType().typeName() + "\n");
            }

            this.error(s.toString());
         }
      }

   }

   public void checkModifiers() {
      if (this.decl().isDeprecated() && !this.withinDeprecatedAnnotation() && this.hostType().topLevelType() != this.decl().hostType().topLevelType() && !this.withinSuppressWarnings("deprecation")) {
         this.warning(this.decl().signature() + " in " + this.decl().hostType().typeName() + " has been deprecated");
      }

   }

   public Collection computeConstraints(GenericMethodDecl decl) {
      Constraints c = new Constraints();

      int i;
      for(i = 0; i < decl.original().getNumTypeParameter(); ++i) {
         c.addTypeVariable(decl.original().getTypeParameter(i));
      }

      TypeDecl R;
      for(i = 0; i < this.getNumArg(); ++i) {
         R = this.getArg(i).type();
         int index = i >= decl.getNumParameter() ? decl.getNumParameter() - 1 : i;
         TypeDecl F = decl.getParameter(index).type();
         if (decl.getParameter(index) instanceof VariableArityParameterDeclaration && (this.getNumArg() != decl.getNumParameter() || !R.isArrayDecl())) {
            F = F.componentType();
         }

         c.convertibleTo(R, F);
      }

      if (c.rawAccess) {
         return new ArrayList();
      } else {
         c.resolveEqualityConstraints();
         c.resolveSupertypeConstraints();
         if (c.unresolvedTypeArguments()) {
            TypeDecl S = this.assignConvertedType();
            if (S.isUnboxedPrimitive()) {
               S = S.boxed();
            }

            R = decl.type();
            if (R.isVoid()) {
               R = this.typeObject();
            }

            c.convertibleFrom(S, R);
            c.resolveEqualityConstraints();
            c.resolveSupertypeConstraints();
            c.resolveSubtypeConstraints();
         }

         return c.typeArguments();
      }
   }

   protected SimpleSet potentiallyApplicable(Collection candidates) {
      SimpleSet potentiallyApplicable = SimpleSet.emptySet;
      Iterator iter = candidates.iterator();

      while(iter.hasNext()) {
         MethodDecl decl = (MethodDecl)iter.next();
         if (this.potentiallyApplicable(decl) && this.accessible(decl)) {
            if (decl instanceof GenericMethodDecl) {
               decl = ((GenericMethodDecl)decl).lookupParMethodDecl(this.typeArguments(decl));
            }

            potentiallyApplicable = potentiallyApplicable.add(decl);
         }
      }

      return potentiallyApplicable;
   }

   protected SimpleSet applicableBySubtyping(SimpleSet potentiallyApplicable) {
      SimpleSet maxSpecific = SimpleSet.emptySet;
      Iterator iter = potentiallyApplicable.iterator();

      while(iter.hasNext()) {
         MethodDecl decl = (MethodDecl)iter.next();
         if (this.applicableBySubtyping(decl)) {
            maxSpecific = mostSpecific(maxSpecific, decl);
         }
      }

      return maxSpecific;
   }

   protected SimpleSet applicableByMethodInvocationConversion(SimpleSet potentiallyApplicable, SimpleSet maxSpecific) {
      if (maxSpecific.isEmpty()) {
         Iterator iter = potentiallyApplicable.iterator();

         while(iter.hasNext()) {
            MethodDecl decl = (MethodDecl)iter.next();
            if (this.applicableByMethodInvocationConversion(decl)) {
               maxSpecific = mostSpecific(maxSpecific, decl);
            }
         }
      }

      return maxSpecific;
   }

   protected SimpleSet applicableVariableArity(SimpleSet potentiallyApplicable, SimpleSet maxSpecific) {
      if (maxSpecific.isEmpty()) {
         Iterator iter = potentiallyApplicable.iterator();

         while(iter.hasNext()) {
            MethodDecl decl = (MethodDecl)iter.next();
            if (decl.isVariableArity() && this.applicableVariableArity(decl)) {
               maxSpecific = mostSpecific(maxSpecific, decl);
            }
         }
      }

      return maxSpecific;
   }

   private static SimpleSet mostSpecific(SimpleSet maxSpecific, MethodDecl decl) {
      if (maxSpecific.isEmpty()) {
         maxSpecific = maxSpecific.add(decl);
      } else if (decl.moreSpecificThan((MethodDecl)maxSpecific.iterator().next())) {
         maxSpecific = SimpleSet.emptySet.add(decl);
      } else if (!((MethodDecl)maxSpecific.iterator().next()).moreSpecificThan(decl)) {
         maxSpecific = maxSpecific.add(decl);
      }

      return maxSpecific;
   }

   private TypeDecl refined_InnerClasses_MethodAccess_methodQualifierType() {
      if (this.hasPrevExpr()) {
         return this.prevExpr().type();
      } else {
         TypeDecl typeDecl;
         for(typeDecl = this.hostType(); typeDecl != null && !typeDecl.hasMethod(this.name()); typeDecl = typeDecl.enclosingType()) {
         }

         return typeDecl != null ? typeDecl : this.decl().hostType();
      }
   }

   public TypeDecl superAccessorTarget() {
      TypeDecl targetDecl = this.prevExpr().type();
      TypeDecl enclosing = this.hostType();

      do {
         enclosing = enclosing.enclosingType();
      } while(!enclosing.instanceOf(targetDecl));

      return enclosing;
   }

   public void refined_Transformations_MethodAccess_transformation() {
      MethodDecl m = this.decl();
      if (this.requiresAccessor()) {
         super.transformation();
         this.replace(this).with(this.decl().createAccessor(this.methodQualifierType()).createBoundAccess(this.getArgList()));
      } else {
         if (!m.isStatic() && this.isQualified() && this.prevExpr().isSuperAccess() && !this.hostType().instanceOf(this.prevExpr().type())) {
            this.decl().createSuperAccessor(this.superAccessorTarget());
         }

         super.transformation();
      }
   }

   public void checkWarnings() {
      MethodDecl decl = this.decl();
      if (decl.getNumParameter() != 0) {
         if (decl.getNumParameter() <= this.getNumArg()) {
            ParameterDeclaration param = decl.getParameter(decl.getNumParameter() - 1);
            if (!this.withinSuppressWarnings("unchecked") && !decl.hasAnnotationSafeVarargs() && param.isVariableArity() && !param.type().isReifiable()) {
               this.warning("unchecked array creation for variable arity parameter of " + this.decl().name());
            }

         }
      }
   }

   public void collectTypesToSignatures(Collection<Type> set) {
      super.collectTypesToSignatures(set);
      this.addDependencyIfNeeded(set, this.methodQualifierType());
   }

   public MethodAccess() {
      this.exceptionCollection_computed = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public MethodAccess(String p0, List<Expr> p1) {
      this.exceptionCollection_computed = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
      this.setID(p0);
      this.setChild(p1, 0);
   }

   public MethodAccess(Symbol p0, List<Expr> p1) {
      this.exceptionCollection_computed = false;
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
      this.setID(p0);
      this.setChild(p1, 0);
   }

   protected int numChildren() {
      return 1;
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

   public void setArgList(List<Expr> list) {
      this.setChild(list, 0);
   }

   public int getNumArg() {
      return this.getArgList().getNumChild();
   }

   public int getNumArgNoTransform() {
      return this.getArgListNoTransform().getNumChildNoTransform();
   }

   public Expr getArg(int i) {
      return (Expr)this.getArgList().getChild(i);
   }

   public void addArg(Expr node) {
      List<Expr> list = this.parent != null && state != null ? this.getArgList() : this.getArgListNoTransform();
      list.addChild(node);
   }

   public void addArgNoTransform(Expr node) {
      List<Expr> list = this.getArgListNoTransform();
      list.addChild(node);
   }

   public void setArg(Expr node, int i) {
      List<Expr> list = this.getArgList();
      list.setChild(node, i);
   }

   public List<Expr> getArgs() {
      return this.getArgList();
   }

   public List<Expr> getArgsNoTransform() {
      return this.getArgListNoTransform();
   }

   public List<Expr> getArgList() {
      List<Expr> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Expr> getArgListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   protected SimpleSet maxSpecific(Collection candidates) {
      SimpleSet potentiallyApplicable = this.potentiallyApplicable(candidates);
      SimpleSet maxSpecific = this.applicableBySubtyping(potentiallyApplicable);
      maxSpecific = this.applicableByMethodInvocationConversion(potentiallyApplicable, maxSpecific);
      maxSpecific = this.applicableVariableArity(potentiallyApplicable, maxSpecific);
      return maxSpecific;
   }

   public void typeCheck() {
      if (this.isQualified() && this.decl().isAbstract() && this.qualifier().isSuperAccess()) {
         this.error("may not access abstract methods in superclass");
      }

      if (!this.decl().isVariableArity() || this.invokesVariableArityAsArray()) {
         for(int i = 0; i < this.decl().getNumParameter(); ++i) {
            TypeDecl exprType = this.getArg(i).type();
            TypeDecl parmType = this.decl().getParameter(i).type();
            if (!exprType.methodInvocationConversionTo(parmType) && !exprType.isUnknown() && !parmType.isUnknown()) {
               this.error("#The type " + exprType.typeName() + " of expr " + this.getArg(i) + " is not compatible with the method parameter " + this.decl().getParameter(i));
            }
         }
      }

   }

   protected TypeDecl refined_GenericsCodegen_MethodAccess_methodQualifierType() {
      TypeDecl typeDecl = this.refined_InnerClasses_MethodAccess_methodQualifierType();
      if (typeDecl == null) {
         return null;
      } else {
         typeDecl = typeDecl.erasure();
         MethodDecl m = this.decl().sourceMethodDecl();
         Collection methods = typeDecl.memberMethods(m.name());
         return !methods.contains(this.decl()) && !methods.contains(m) ? m.hostType() : typeDecl.erasure();
      }
   }

   public void transformation() {
      if (this.decl().isVariableArity() && !this.invokesVariableArityAsArray()) {
         List list = new List();

         for(int i = 0; i < this.decl().getNumParameter() - 1; ++i) {
            list.add(this.getArg(i).fullCopy());
         }

         List last = new List();

         for(int i = this.decl().getNumParameter() - 1; i < this.getNumArg(); ++i) {
            last.add(this.getArg(i).fullCopy());
         }

         Access typeAccess = this.decl().lastParameter().type().elementType().createQualifiedAccess();

         for(int i = 0; i < this.decl().lastParameter().type().dimension(); ++i) {
            typeAccess = new ArrayTypeAccess((Access)typeAccess);
         }

         list.add(new ArrayCreationExpr((Access)typeAccess, new Opt(new ArrayInit(last))));
         this.setArgList(list);
      }

      this.refined_Transformations_MethodAccess_transformation();
   }

   private ArrayList buildArgList(Body b) {
      ArrayList list = new ArrayList();

      for(int i = 0; i < this.getNumArg(); ++i) {
         list.add(this.asImmediate(b, this.getArg(i).type().emitCastTo(b, this.getArg(i), this.decl().getParameter(i).type())));
      }

      return list;
   }

   public Value eval(Body b) {
      MethodDecl decl = this.decl().erasedMethod();
      ArrayList list;
      if (!this.decl().isStatic() && this.isQualified() && this.prevExpr().isSuperAccess()) {
         Local left = this.asLocal(b, this.createLoadQualifier(b));
         list = this.buildArgList(b);
         Object result;
         if (!this.hostType().instanceOf(this.prevExpr().type())) {
            MethodDecl m = decl.createSuperAccessor(this.superAccessorTarget());
            if (this.methodQualifierType().isInterfaceDecl()) {
               result = b.newInterfaceInvokeExpr(left, m.sootRef(), (java.util.List)list, this);
            } else {
               result = b.newVirtualInvokeExpr(left, m.sootRef(), (java.util.List)list, this);
            }
         } else {
            result = b.newSpecialInvokeExpr(left, this.sootRef(), (java.util.List)list, this);
         }

         if (decl.type() != this.decl().type()) {
            result = decl.type().emitCastTo(b, (Value)result, this.decl().type(), this);
         }

         return (Value)(this.type().isVoid() ? result : this.asLocal(b, (Value)result));
      } else {
         Object result;
         if (!this.decl().isStatic()) {
            Local left = this.asLocal(b, this.createLoadQualifier(b));
            ArrayList list = this.buildArgList(b);
            if (this.methodQualifierType().isInterfaceDecl()) {
               result = b.newInterfaceInvokeExpr(left, this.sootRef(), (java.util.List)list, this);
            } else {
               result = b.newVirtualInvokeExpr(left, this.sootRef(), (java.util.List)list, this);
            }
         } else {
            if (this.isQualified() && !this.qualifier().isTypeAccess()) {
               b.newTemp(this.qualifier().eval(b));
            }

            list = this.buildArgList(b);
            result = b.newStaticInvokeExpr(this.sootRef(), (java.util.List)list, this);
         }

         if (decl.type() != this.decl().type()) {
            result = decl.type().emitCastTo(b, (Value)result, this.decl().type(), this);
         }

         return (Value)(this.type().isVoid() ? result : this.asLocal(b, (Value)result));
      }
   }

   private SootMethodRef sootRef() {
      MethodDecl decl = this.decl().erasedMethod();
      ArrayList parameters = new ArrayList();

      for(int i = 0; i < decl.getNumParameter(); ++i) {
         parameters.add(decl.getParameter(i).type().getSootType());
      }

      SootMethodRef ref = Scene.v().makeMethodRef(this.methodQualifierType().getSootClassDecl(), decl.name(), parameters, decl.type().getSootType(), decl.isStatic());
      return ref;
   }

   private Value createLoadQualifier(Body b) {
      MethodDecl m = this.decl().erasedMethod();
      if (this.hasPrevExpr()) {
         Value v = this.prevExpr().eval(b);
         if (v == null) {
            throw new Error("Problems evaluating " + this.prevExpr().getClass().getName());
         } else {
            Local qualifier = this.asLocal(b, v);
            return qualifier;
         }
      } else if (!m.isStatic()) {
         return this.emitThis(b, this.methodQualifierType());
      } else {
         throw new Error("createLoadQualifier not supported for " + m.getClass().getName());
      }
   }

   protected TypeDecl methodQualifierType() {
      TypeDecl typeDecl = this.refined_GenericsCodegen_MethodAccess_methodQualifierType();
      return typeDecl != null ? typeDecl : this.decl().hostType();
   }

   private TypeDecl refined_TypeAnalysis_MethodAccess_type() {
      return this.decl().type();
   }

   public boolean computeDAbefore(int i, Variable v) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(i);
      _parameters.add(v);
      if (this.computeDAbefore_int_Variable_values == null) {
         this.computeDAbefore_int_Variable_values = new HashMap(4);
      }

      if (this.computeDAbefore_int_Variable_values.containsKey(_parameters)) {
         return (Boolean)this.computeDAbefore_int_Variable_values.get(_parameters);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean computeDAbefore_int_Variable_value = this.computeDAbefore_compute(i, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.computeDAbefore_int_Variable_values.put(_parameters, computeDAbefore_int_Variable_value);
         }

         return computeDAbefore_int_Variable_value;
      }
   }

   private boolean computeDAbefore_compute(int i, Variable v) {
      return i == 0 ? this.isDAbefore(v) : this.getArg(i - 1).isDAafter(v);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumArg() == 0 ? this.isDAbefore(v) : this.getArg(this.getNumArg() - 1).isDAafter(v);
   }

   public boolean isDAafterTrue(Variable v) {
      boolean var10000;
      label18: {
         ASTNode$State state = this.state();
         if (this.getNumArg() == 0) {
            if (this.isDAbefore(v)) {
               break label18;
            }
         } else if (this.getArg(this.getNumArg() - 1).isDAafter(v)) {
            break label18;
         }

         if (!this.isFalse()) {
            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   public boolean isDAafterFalse(Variable v) {
      boolean var10000;
      label18: {
         ASTNode$State state = this.state();
         if (this.getNumArg() == 0) {
            if (this.isDAbefore(v)) {
               break label18;
            }
         } else if (this.getArg(this.getNumArg() - 1).isDAafter(v)) {
            break label18;
         }

         if (!this.isTrue()) {
            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   public Collection exceptionCollection() {
      if (this.exceptionCollection_computed) {
         return this.exceptionCollection_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.exceptionCollection_value = this.exceptionCollection_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.exceptionCollection_computed = true;
         }

         return this.exceptionCollection_value;
      }
   }

   private Collection exceptionCollection_compute() {
      HashSet set = new HashSet();
      Iterator iter = this.decls().iterator();
      if (!iter.hasNext()) {
         return set;
      } else {
         MethodDecl m = (MethodDecl)iter.next();

         for(int i = 0; i < m.getNumException(); ++i) {
            TypeDecl exceptionType = m.getException(i).type();
            set.add(exceptionType);
         }

         while(iter.hasNext()) {
            HashSet first = new HashSet();
            first.addAll(set);
            HashSet second = new HashSet();
            m = (MethodDecl)iter.next();

            TypeDecl firstType;
            for(int i = 0; i < m.getNumException(); ++i) {
               firstType = m.getException(i).type();
               second.add(firstType);
            }

            set = new HashSet();
            Iterator i1 = first.iterator();

            while(i1.hasNext()) {
               firstType = (TypeDecl)i1.next();
               Iterator i2 = second.iterator();

               while(i2.hasNext()) {
                  TypeDecl secondType = (TypeDecl)i2.next();
                  if (firstType.instanceOf(secondType)) {
                     set.add(firstType);
                  } else if (secondType.instanceOf(firstType)) {
                     set.add(secondType);
                  }
               }
            }
         }

         return set;
      }
   }

   public MethodDecl singleCandidateDecl() {
      ASTNode$State state = this.state();
      MethodDecl result = null;
      Iterator iter = this.lookupMethod(this.name()).iterator();

      while(iter.hasNext()) {
         MethodDecl m = (MethodDecl)iter.next();
         if (result == null) {
            result = m;
         } else if (m.getNumParameter() == this.getNumArg() && result.getNumParameter() != this.getNumArg()) {
            result = m;
         }
      }

      return result;
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
      SimpleSet maxSpecific = this.maxSpecific(this.lookupMethod(this.name()));
      if (this.isQualified()) {
         if (!this.qualifier().staticContextQualifier()) {
            return maxSpecific;
         }
      } else if (!this.inStaticContext()) {
         return maxSpecific;
      }

      maxSpecific = removeInstanceMethods(maxSpecific);
      return maxSpecific;
   }

   public MethodDecl decl() {
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

   private MethodDecl decl_compute() {
      SimpleSet decls = this.decls();
      if (decls.size() == 1) {
         return (MethodDecl)decls.iterator().next();
      } else {
         boolean allAbstract = true;
         Iterator iter = decls.iterator();

         while(iter.hasNext() && allAbstract) {
            MethodDecl m = (MethodDecl)iter.next();
            if (!m.isAbstract() && !m.hostType().isObject()) {
               allAbstract = false;
            }
         }

         return decls.size() > 1 && allAbstract ? (MethodDecl)decls.iterator().next() : this.unknownMethod();
      }
   }

   public boolean accessible(MethodDecl m) {
      ASTNode$State state = this.state();
      if (!this.isQualified()) {
         return true;
      } else if (!m.accessibleFrom(this.hostType())) {
         return false;
      } else if (!this.qualifier().type().accessibleFrom(this.hostType())) {
         return false;
      } else {
         return m.isProtected() && !m.hostPackage().equals(this.hostPackage()) && !m.isStatic() && !this.qualifier().isSuperAccess() ? this.hostType().mayAccess(this, m) : true;
      }
   }

   public boolean validArgs() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumArg(); ++i) {
         if (this.getArg(i).type().isUnknown()) {
            return false;
         }
      }

      return true;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public boolean isMethodAccess() {
      ASTNode$State state = this.state();
      return true;
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
      if (this.getNumArg() == 0 && this.name().equals("getClass") && this.decl().hostType().isObject()) {
         TypeDecl bound = this.isQualified() ? this.qualifier().type() : this.hostType();
         ArrayList args = new ArrayList();
         args.add(bound.erasure().asWildcardExtends());
         return ((GenericClassDecl)this.lookupType("java.lang", "Class")).lookupParTypeDecl(args);
      } else {
         return this.refined_TypeAnalysis_MethodAccess_type();
      }
   }

   public boolean applicableBySubtyping(MethodDecl m) {
      ASTNode$State state = this.state();
      if (m.getNumParameter() != this.getNumArg()) {
         return false;
      } else {
         for(int i = 0; i < m.getNumParameter(); ++i) {
            if (!this.getArg(i).type().instanceOf(m.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean applicableByMethodInvocationConversion(MethodDecl m) {
      ASTNode$State state = this.state();
      if (m.getNumParameter() != this.getNumArg()) {
         return false;
      } else {
         for(int i = 0; i < m.getNumParameter(); ++i) {
            if (!this.getArg(i).type().methodInvocationConversionTo(m.getParameter(i).type())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean applicableVariableArity(MethodDecl m) {
      ASTNode$State state = this.state();

      int i;
      for(i = 0; i < m.getNumParameter() - 1; ++i) {
         if (!this.getArg(i).type().methodInvocationConversionTo(m.getParameter(i).type())) {
            return false;
         }
      }

      for(i = m.getNumParameter() - 1; i < this.getNumArg(); ++i) {
         if (!this.getArg(i).type().methodInvocationConversionTo(m.lastParameter().type().componentType())) {
            return false;
         }
      }

      return true;
   }

   public boolean potentiallyApplicable(MethodDecl m) {
      ASTNode$State state = this.state();
      if (!m.name().equals(this.name())) {
         return false;
      } else if (!m.accessibleFrom(this.hostType())) {
         return false;
      } else if (m.isVariableArity() && this.arity() < m.arity() - 1) {
         return false;
      } else if (!m.isVariableArity() && m.arity() != this.arity()) {
         return false;
      } else {
         if (m instanceof GenericMethodDecl) {
            GenericMethodDecl gm = (GenericMethodDecl)m;
            ArrayList list = this.typeArguments(m);
            if (list.size() != 0) {
               if (gm.getNumTypeParameter() != list.size()) {
                  return false;
               }

               for(int i = 0; i < gm.getNumTypeParameter(); ++i) {
                  if (!((TypeDecl)list.get(i)).subtype(gm.original().getTypeParameter(i))) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   public int arity() {
      ASTNode$State state = this.state();
      return this.getNumArg();
   }

   public ArrayList typeArguments(MethodDecl m) {
      if (this.typeArguments_MethodDecl_values == null) {
         this.typeArguments_MethodDecl_values = new HashMap(4);
      }

      if (this.typeArguments_MethodDecl_values.containsKey(m)) {
         return (ArrayList)this.typeArguments_MethodDecl_values.get(m);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         ArrayList typeArguments_MethodDecl_value = this.typeArguments_compute(m);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.typeArguments_MethodDecl_values.put(m, typeArguments_MethodDecl_value);
         }

         return typeArguments_MethodDecl_value;
      }
   }

   private ArrayList typeArguments_compute(MethodDecl m) {
      ArrayList typeArguments = new ArrayList();
      if (m instanceof GenericMethodDecl) {
         GenericMethodDecl g = (GenericMethodDecl)m;
         Collection arguments = this.computeConstraints(g);
         if (arguments.isEmpty()) {
            return typeArguments;
         }

         int i = 0;

         for(Iterator iter = arguments.iterator(); iter.hasNext(); ++i) {
            TypeDecl typeDecl = (TypeDecl)iter.next();
            if (typeDecl == null) {
               TypeVariable v = g.original().getTypeParameter(i);
               if (v.getNumTypeBound() == 0) {
                  typeDecl = this.typeObject();
               } else if (v.getNumTypeBound() == 1) {
                  typeDecl = v.getTypeBound(0).type();
               } else {
                  typeDecl = v.lubType();
               }
            }

            typeArguments.add(typeDecl);
         }
      }

      return typeArguments;
   }

   public boolean invokesVariableArityAsArray() {
      ASTNode$State state = this.state();
      if (!this.decl().isVariableArity()) {
         return false;
      } else {
         return this.arity() != this.decl().arity() ? false : this.getArg(this.getNumArg() - 1).type().methodInvocationConversionTo(this.decl().lastParameter().type());
      }
   }

   public boolean requiresAccessor() {
      ASTNode$State state = this.state();
      MethodDecl m = this.decl();
      if (m.isPrivate() && m.hostType() != this.hostType()) {
         return true;
      } else {
         return m.isProtected() && !m.hostPackage().equals(this.hostPackage()) && !this.hostType().hasMethod(m.name());
      }
   }

   public boolean handlesException(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
      return handlesException_TypeDecl_value;
   }

   public MethodDecl unknownMethod() {
      ASTNode$State state = this.state();
      MethodDecl unknownMethod_value = this.getParent().Define_MethodDecl_unknownMethod(this, (ASTNode)null);
      return unknownMethod_value;
   }

   public boolean inExplicitConstructorInvocation() {
      ASTNode$State state = this.state();
      boolean inExplicitConstructorInvocation_value = this.getParent().Define_boolean_inExplicitConstructorInvocation(this, (ASTNode)null);
      return inExplicitConstructorInvocation_value;
   }

   public TypeDecl typeObject() {
      ASTNode$State state = this.state();
      TypeDecl typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
      return typeObject_value;
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getArgListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return this.computeDAbefore(i, v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupMethod(name);
      } else {
         return this.getParent().Define_Collection_lookupMethod(this, caller, name);
      }
   }

   public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().hasPackage(packageName);
      } else {
         return this.getParent().Define_boolean_hasPackage(this, caller, packageName);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupType(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupType(this, caller, name);
      }
   }

   public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupVariable(name);
      } else {
         return this.getParent().Define_SimpleSet_lookupVariable(this, caller, name);
      }
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.EXPRESSION_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public String Define_String_methodHost(ASTNode caller, ASTNode child) {
      this.getIndexOfChild(caller);
      return this.unqualifiedScope().methodHost();
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.typeObject();
      } else {
         return this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
