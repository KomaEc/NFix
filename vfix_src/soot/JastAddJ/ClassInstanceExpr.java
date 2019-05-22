package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;

public class ClassInstanceExpr extends Access implements Cloneable {
   protected boolean addEnclosingVariables;
   protected Map isDAafterInstance_Variable_values;
   protected Map computeDAbefore_int_Variable_values;
   protected Map computeDUbefore_int_Variable_values;
   protected boolean decls_computed;
   protected SimpleSet decls_value;
   protected boolean decl_computed;
   protected ConstructorDecl decl_value;
   protected Map localLookupType_String_values;
   protected boolean type_computed;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafterInstance_Variable_values = null;
      this.computeDAbefore_int_Variable_values = null;
      this.computeDUbefore_int_Variable_values = null;
      this.decls_computed = false;
      this.decls_value = null;
      this.decl_computed = false;
      this.decl_value = null;
      this.localLookupType_String_values = null;
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ClassInstanceExpr clone() throws CloneNotSupportedException {
      ClassInstanceExpr node = (ClassInstanceExpr)super.clone();
      node.isDAafterInstance_Variable_values = null;
      node.computeDAbefore_int_Variable_values = null;
      node.computeDUbefore_int_Variable_values = null;
      node.decls_computed = false;
      node.decls_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.localLookupType_String_values = null;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ClassInstanceExpr copy() {
      try {
         ClassInstanceExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ClassInstanceExpr fullCopy() {
      ClassInstanceExpr tree = this.copy();
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

   public void accessControl() {
      super.accessControl();
      if (this.type().isAbstract()) {
         this.error("Can not instantiate abstract class " + this.type().fullName());
      }

      if (!this.decl().accessibleFrom(this.hostType())) {
         this.error("constructor " + this.decl().signature() + " is not accessible");
      }

   }

   public void exceptionHandling() {
      Iterator var1 = this.decl().getExceptionList().iterator();

      while(var1.hasNext()) {
         Access exception = (Access)var1.next();
         TypeDecl exceptionType = exception.type();
         if (!this.handlesException(exceptionType)) {
            this.error("" + this + " may throw uncaught exception " + exceptionType.fullName() + "; it must be caught or declared as being thrown");
         }
      }

   }

   protected boolean reachedException(TypeDecl catchType) {
      ConstructorDecl decl = this.decl();

      int i;
      for(i = 0; i < decl.getNumException(); ++i) {
         TypeDecl exceptionType = decl.getException(i).type();
         if (catchType.mayCatch(exceptionType)) {
            return true;
         }
      }

      for(i = 0; i < this.getNumArg(); ++i) {
         if (this.getArg(i).reachedException(catchType)) {
            return true;
         }
      }

      return false;
   }

   public SimpleSet keepInnerClasses(SimpleSet c) {
      SimpleSet newSet = SimpleSet.emptySet;
      Iterator iter = c.iterator();

      while(iter.hasNext()) {
         TypeDecl t = (TypeDecl)iter.next();
         if (t.isInnerType() && t.isClassDecl()) {
            newSet = newSet.add(c);
         }
      }

      return newSet;
   }

   public void refined_NameCheck_ClassInstanceExpr_nameCheck() {
      super.nameCheck();
      if (this.decls().isEmpty()) {
         this.error("can not instantiate " + this.type().typeName() + " no matching constructor found in " + this.type().typeName());
      } else if (this.decls().size() > 1 && this.validArgs()) {
         this.error("several most specific constructors found");
         Iterator iter = this.decls().iterator();

         while(iter.hasNext()) {
            this.error("         " + ((ConstructorDecl)iter.next()).signature());
         }
      }

   }

   public ClassInstanceExpr(Access type, List args) {
      this(type, args, new Opt());
   }

   public void toString(StringBuffer s) {
      s.append("new ");
      this.getAccess().toString(s);
      s.append("(");
      if (this.getNumArg() > 0) {
         this.getArg(0).toString(s);

         for(int i = 1; i < this.getNumArg(); ++i) {
            s.append(", ");
            this.getArg(i).toString(s);
         }
      }

      s.append(")");
      if (this.hasTypeDecl()) {
         TypeDecl decl = this.getTypeDecl();
         s.append(" {");

         for(int i = 0; i < decl.getNumBodyDecl(); ++i) {
            if (!(decl.getBodyDecl(i) instanceof ConstructorDecl)) {
               decl.getBodyDecl(i).toString(s);
            }
         }

         s.append(this.typeDeclIndent());
         s.append("}");
      }

   }

   public void typeCheck() {
      if (this.isQualified() && this.qualifier().isTypeAccess() && !this.qualifier().type().isUnknown()) {
         this.error("*** The expression in a qualified class instance expr must not be a type name");
      }

      if (this.isQualified() && !this.type().isInnerClass() && !((ClassDecl)this.type()).superclass().isInnerClass() && !this.type().isUnknown()) {
         this.error("*** Qualified class instance creation can only instantiate inner classes and their anonymous subclasses");
      }

      if (!this.type().isClassDecl()) {
         this.error("*** Can only instantiate classes, which " + this.type().typeName() + " is not");
      }

      this.typeCheckEnclosingInstance();
      this.typeCheckAnonymousSuperclassEnclosingInstance();
   }

   public void typeCheckEnclosingInstance() {
      TypeDecl C = this.type();
      if (C.isInnerClass()) {
         TypeDecl enclosing = null;
         if (C.isAnonymous()) {
            if (this.noEnclosingInstance()) {
               enclosing = null;
            } else {
               enclosing = this.hostType();
            }
         } else {
            TypeDecl nest;
            if (C.isLocalClass()) {
               if (C.inStaticContext()) {
                  enclosing = null;
               } else if (this.noEnclosingInstance()) {
                  enclosing = this.unknownType();
               } else {
                  for(nest = this.hostType(); nest != null && !nest.instanceOf(C.enclosingType()); nest = nest.enclosingType()) {
                  }

                  enclosing = nest;
               }
            } else if (C.isMemberType()) {
               if (!this.isQualified()) {
                  if (this.noEnclosingInstance()) {
                     this.error("No enclosing instance to initialize " + C.typeName() + " with");
                     enclosing = this.unknownType();
                  } else {
                     for(nest = this.hostType(); nest != null && !nest.instanceOf(C.enclosingType()); nest = nest.enclosingType()) {
                     }

                     enclosing = nest == null ? this.unknownType() : nest;
                  }
               } else {
                  enclosing = this.enclosingInstance();
               }
            }
         }

         if (enclosing != null && !enclosing.instanceOf(this.type().enclosingType())) {
            String msg = enclosing == null ? "None" : enclosing.typeName();
            this.error("*** Can not instantiate " + this.type().typeName() + " with the enclosing instance " + msg + " due to incorrect enclosing instance");
         } else if (!this.isQualified() && C.isMemberType() && this.inExplicitConstructorInvocation() && enclosing == this.hostType()) {
            this.error("*** The innermost enclosing instance of type " + enclosing.typeName() + " is this which is not yet initialized here.");
         }

      }
   }

   public void typeCheckAnonymousSuperclassEnclosingInstance() {
      if (this.type().isAnonymous() && ((ClassDecl)this.type()).superclass().isInnerType()) {
         TypeDecl S = ((ClassDecl)this.type()).superclass();
         if (S.isLocalClass()) {
            if (!S.inStaticContext()) {
               if (this.noEnclosingInstance()) {
                  this.error("*** No enclosing instance to class " + this.type().typeName() + " due to static context");
               } else if (this.inExplicitConstructorInvocation()) {
                  this.error("*** No enclosing instance to superclass " + S.typeName() + " of " + this.type().typeName() + " since this is not initialized yet");
               }
            }
         } else if (S.isMemberType() && !this.isQualified()) {
            if (this.noEnclosingInstance()) {
               this.error("*** No enclosing instance to class " + this.type().typeName() + " due to static context");
            } else {
               TypeDecl nest;
               for(nest = this.hostType(); nest != null && !nest.instanceOf(S.enclosingType()); nest = nest.enclosingType()) {
               }

               if (nest == null) {
                  this.error("*** No enclosing instance to superclass " + S.typeName() + " of " + this.type().typeName());
               } else if (this.inExplicitConstructorInvocation()) {
                  this.error("*** No enclosing instance to superclass " + S.typeName() + " of " + this.type().typeName() + " since this is not initialized yet");
               }
            }
         }
      }

   }

   public void checkModifiers() {
      if (this.decl().isDeprecated() && !this.withinDeprecatedAnnotation() && this.hostType().topLevelType() != this.decl().hostType().topLevelType() && !this.withinSuppressWarnings("deprecation")) {
         this.warning(this.decl().signature() + " in " + this.decl().hostType().typeName() + " has been deprecated");
      }

   }

   public void addEnclosingVariables() {
      if (this.addEnclosingVariables) {
         this.addEnclosingVariables = false;
         this.decl().addEnclosingVariables();
         Iterator iter = this.decl().hostType().enclosingVariables().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            this.getArgList().add(new VarAccess(v.name()));
         }

      }
   }

   public void refined_Transformations_ClassInstanceExpr_transformation() {
      this.addEnclosingVariables();
      if (this.decl().isPrivate() && this.type() != this.hostType()) {
         this.decl().createAccessor();
      }

      super.transformation();
   }

   private Value emitLocalEnclosing(Body b, TypeDecl localClass) {
      if (!localClass.inStaticContext()) {
         return this.emitThis(b, localClass.enclosingType());
      } else {
         throw new Error("Not implemented");
      }
   }

   private Value emitInnerMemberEnclosing(Body b, TypeDecl innerClass) {
      if (this.hasPrevExpr()) {
         Local base = this.asLocal(b, this.prevExpr().eval(b));
         b.setLine(this);
         b.add(b.newInvokeStmt(b.newVirtualInvokeExpr(base, Scene.v().getMethod("<java.lang.Object: java.lang.Class getClass()>").makeRef(), this), this));
         return base;
      } else {
         TypeDecl enclosing;
         for(enclosing = this.hostType(); !enclosing.hasType(innerClass.name()); enclosing = enclosing.enclosingType()) {
         }

         return this.emitThis(b, enclosing);
      }
   }

   public Value eval(Body b) {
      Local local = this.asLocal(b, b.newNewExpr(this.type().sootRef(), this));
      ArrayList list = new ArrayList();
      if (this.type().isAnonymous()) {
         if (this.type().isAnonymousInNonStaticContext()) {
            list.add(this.asImmediate(b, b.emitThis(this.hostType())));
         }

         ClassDecl C = (ClassDecl)this.type();
         TypeDecl S = C.superclass();
         if (S.isLocalClass()) {
            if (!this.type().inStaticContext()) {
               list.add(this.asImmediate(b, this.emitLocalEnclosing(b, S)));
            }
         } else if (S.isInnerType()) {
            list.add(this.asImmediate(b, this.emitInnerMemberEnclosing(b, S)));
         }
      } else if (this.type().isLocalClass()) {
         if (!this.type().inStaticContext()) {
            list.add(this.asImmediate(b, this.emitLocalEnclosing(b, this.type())));
         }
      } else if (this.type().isInnerType()) {
         list.add(this.asImmediate(b, this.emitInnerMemberEnclosing(b, this.type())));
      }

      for(int i = 0; i < this.getNumArg(); ++i) {
         list.add(this.asImmediate(b, this.getArg(i).type().emitCastTo(b, this.getArg(i), this.decl().getParameter(i).type())));
      }

      if (this.decl().isPrivate() && this.type() != this.hostType()) {
         list.add(this.asImmediate(b, NullConstant.v()));
         b.setLine(this);
         b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, this.decl().createAccessor().sootRef(), (java.util.List)list, this), this));
         return local;
      } else {
         b.setLine(this);
         b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(local, this.decl().sootRef(), (java.util.List)list, this), this));
         return local;
      }
   }

   public void collectTypesToSignatures(Collection<Type> set) {
      super.collectTypesToSignatures(set);
      this.addDependencyIfNeeded(set, this.decl().erasedConstructor().hostType());
   }

   public ClassInstanceExpr() {
      this.addEnclosingVariables = true;
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
   }

   public ClassInstanceExpr(Access p0, List<Expr> p1, Opt<TypeDecl> p2) {
      this.addEnclosingVariables = true;
      this.decls_computed = false;
      this.decl_computed = false;
      this.type_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setAccess(Access node) {
      this.setChild(node, 0);
   }

   public Access getAccess() {
      return (Access)this.getChild(0);
   }

   public Access getAccessNoTransform() {
      return (Access)this.getChildNoTransform(0);
   }

   public void setArgList(List<Expr> list) {
      this.setChild(list, 1);
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
      List<Expr> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Expr> getArgListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setTypeDeclOpt(Opt<TypeDecl> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasTypeDecl() {
      return this.getTypeDeclOpt().getNumChild() != 0;
   }

   public TypeDecl getTypeDecl() {
      return (TypeDecl)this.getTypeDeclOpt().getChild(0);
   }

   public void setTypeDecl(TypeDecl node) {
      this.getTypeDeclOpt().setChild(node, 0);
   }

   public Opt<TypeDecl> getTypeDeclOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<TypeDecl> getTypeDeclOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public void nameCheck() {
      if (this.getAccess().type().isEnumDecl() && !this.enclosingBodyDecl().isEnumConstant()) {
         this.error("enum types may not be instantiated explicitly");
      } else {
         this.refined_NameCheck_ClassInstanceExpr_nameCheck();
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

      this.refined_Transformations_ClassInstanceExpr_transformation();
   }

   public boolean isDAafterInstance(Variable v) {
      if (this.isDAafterInstance_Variable_values == null) {
         this.isDAafterInstance_Variable_values = new HashMap(4);
      }

      if (this.isDAafterInstance_Variable_values.containsKey(v)) {
         return (Boolean)this.isDAafterInstance_Variable_values.get(v);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         boolean isDAafterInstance_Variable_value = this.isDAafterInstance_compute(v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isDAafterInstance_Variable_values.put(v, isDAafterInstance_Variable_value);
         }

         return isDAafterInstance_Variable_value;
      }
   }

   private boolean isDAafterInstance_compute(Variable v) {
      return this.getNumArg() == 0 ? this.isDAbefore(v) : this.getArg(this.getNumArg() - 1).isDAafter(v);
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDAafterInstance(v);
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

   public boolean isDUafterInstance(Variable v) {
      ASTNode$State state = this.state();
      return this.getNumArg() == 0 ? this.isDUbefore(v) : this.getArg(this.getNumArg() - 1).isDUafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.isDUafterInstance(v);
   }

   public boolean computeDUbefore(int i, Variable v) {
      java.util.List _parameters = new ArrayList(2);
      _parameters.add(i);
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
         boolean computeDUbefore_int_Variable_value = this.computeDUbefore_compute(i, v);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.computeDUbefore_int_Variable_values.put(_parameters, computeDUbefore_int_Variable_value);
         }

         return computeDUbefore_int_Variable_value;
      }
   }

   private boolean computeDUbefore_compute(int i, Variable v) {
      return i == 0 ? this.isDUbefore(v) : this.getArg(i - 1).isDUafter(v);
   }

   public boolean applicableAndAccessible(ConstructorDecl decl) {
      ASTNode$State state = this.state();
      return decl.applicable(this.getArgList()) && decl.accessibleFrom(this.hostType()) && (!decl.isProtected() || this.hasTypeDecl() || decl.hostPackage().equals(this.hostPackage()));
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
      TypeDecl typeDecl = this.hasTypeDecl() ? this.getTypeDecl() : this.getAccess().type();
      return this.chooseConstructor(typeDecl.constructors(), this.getArgList());
   }

   public ConstructorDecl decl() {
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

   private ConstructorDecl decl_compute() {
      SimpleSet decls = this.decls();
      return decls.size() == 1 ? (ConstructorDecl)decls.iterator().next() : this.unknownConstructor();
   }

   public SimpleSet qualifiedLookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet c = this.keepAccessibleTypes(this.type().memberTypes(name));
      if (!c.isEmpty()) {
         return c;
      } else {
         return this.type().name().equals(name) ? SimpleSet.emptySet.add(this.type()) : SimpleSet.emptySet;
      }
   }

   public SimpleSet localLookupType(String name) {
      if (this.localLookupType_String_values == null) {
         this.localLookupType_String_values = new HashMap(4);
      }

      if (this.localLookupType_String_values.containsKey(name)) {
         return (SimpleSet)this.localLookupType_String_values.get(name);
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         SimpleSet localLookupType_String_value = this.localLookupType_compute(name);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localLookupType_String_values.put(name, localLookupType_String_value);
         }

         return localLookupType_String_value;
      }
   }

   private SimpleSet localLookupType_compute(String name) {
      return this.hasTypeDecl() && this.getTypeDecl().name().equals(name) ? SimpleSet.emptySet.add(this.getTypeDecl()) : SimpleSet.emptySet;
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

   public NameType predNameType() {
      ASTNode$State state = this.state();
      return NameType.EXPRESSION_NAME;
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
      return this.hasTypeDecl() ? this.getTypeDecl() : this.getAccess().type();
   }

   public boolean noEnclosingInstance() {
      ASTNode$State state = this.state();
      return this.isQualified() ? this.qualifier().staticContextQualifier() : this.inStaticContext();
   }

   public int arity() {
      ASTNode$State state = this.state();
      return this.getNumArg();
   }

   public boolean invokesVariableArityAsArray() {
      ASTNode$State state = this.state();
      if (!this.decl().isVariableArity()) {
         return false;
      } else {
         return this.arity() != this.decl().arity() ? false : this.getArg(this.getNumArg() - 1).type().methodInvocationConversionTo(this.decl().lastParameter().type());
      }
   }

   public boolean handlesException(TypeDecl exceptionType) {
      ASTNode$State state = this.state();
      boolean handlesException_TypeDecl_value = this.getParent().Define_boolean_handlesException(this, (ASTNode)null, exceptionType);
      return handlesException_TypeDecl_value;
   }

   public TypeDecl typeObject() {
      ASTNode$State state = this.state();
      TypeDecl typeObject_value = this.getParent().Define_TypeDecl_typeObject(this, (ASTNode)null);
      return typeObject_value;
   }

   public ConstructorDecl unknownConstructor() {
      ASTNode$State state = this.state();
      ConstructorDecl unknownConstructor_value = this.getParent().Define_ConstructorDecl_unknownConstructor(this, (ASTNode)null);
      return unknownConstructor_value;
   }

   public String typeDeclIndent() {
      ASTNode$State state = this.state();
      String typeDeclIndent_value = this.getParent().Define_String_typeDeclIndent(this, (ASTNode)null);
      return typeDeclIndent_value;
   }

   public TypeDecl enclosingInstance() {
      ASTNode$State state = this.state();
      TypeDecl enclosingInstance_value = this.getParent().Define_TypeDecl_enclosingInstance(this, (ASTNode)null);
      return enclosingInstance_value;
   }

   public boolean inExplicitConstructorInvocation() {
      ASTNode$State state = this.state();
      boolean inExplicitConstructorInvocation_value = this.getParent().Define_boolean_inExplicitConstructorInvocation(this, (ASTNode)null);
      return inExplicitConstructorInvocation_value;
   }

   public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeDeclOptNoTransform() ? this.getAccess().type() : this.getParent().Define_TypeDecl_superType(this, caller);
   }

   public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeDeclOptNoTransform()) {
         Collection c = this.getAccess().type().constructors();
         SimpleSet maxSpecific = this.chooseConstructor(c, this.getArgList());
         return maxSpecific.size() == 1 ? (ConstructorDecl)maxSpecific.iterator().next() : this.unknownConstructor();
      } else {
         return this.getParent().Define_ConstructorDecl_constructorDecl(this, caller);
      }
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getTypeDeclOptNoTransform()) {
         return this.isDAafterInstance(v);
      } else if (caller == this.getArgListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return this.computeDAbefore(i, v);
      } else {
         return this.getParent().Define_boolean_isDAbefore(this, caller, v);
      }
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      if (caller == this.getArgListNoTransform()) {
         int i = caller.getIndexOfChild(child);
         return this.computeDUbefore(i, v);
      } else {
         return this.getParent().Define_boolean_isDUbefore(this, caller, v);
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
      SimpleSet c;
      if (caller == this.getTypeDeclOptNoTransform()) {
         c = this.localLookupType(name);
         if (!c.isEmpty()) {
            return c;
         } else {
            c = this.lookupType(name);
            return !c.isEmpty() ? c : this.unqualifiedScope().lookupType(name);
         }
      } else if (caller == this.getAccessNoTransform()) {
         c = this.lookupType(name);
         if (c.size() == 1 && this.isQualified()) {
            c = this.keepInnerClasses(c);
         }

         return c;
      } else if (caller == this.getArgListNoTransform()) {
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
      } else if (caller == this.getTypeDeclOptNoTransform()) {
         return NameType.TYPE_NAME;
      } else {
         return caller == this.getAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
      return caller == this.getTypeDeclOptNoTransform() ? true : this.getParent().Define_boolean_isAnonymous(this, caller);
   }

   public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeDeclOptNoTransform() ? false : this.getParent().Define_boolean_isMemberType(this, caller);
   }

   public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeDeclOptNoTransform() ? this.hostType() : this.getParent().Define_TypeDecl_hostType(this, caller);
   }

   public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeDeclOptNoTransform()) {
         return this.isQualified() ? this.qualifier().staticContextQualifier() : this.inStaticContext();
      } else {
         return this.getParent().Define_boolean_inStaticContext(this, caller);
      }
   }

   public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? this : this.getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
   }

   public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? this.hasTypeDecl() : this.getParent().Define_boolean_isAnonymousDecl(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
