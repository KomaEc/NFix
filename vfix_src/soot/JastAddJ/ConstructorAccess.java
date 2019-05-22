package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.NullConstant;

public class ConstructorAccess extends Access implements Cloneable {
   protected boolean addEnclosingVariables = true;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected boolean decls_computed = false;
   protected SimpleSet decls_value;
   protected boolean decl_computed = false;
   protected ConstructorDecl decl_value;
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.decls_computed = false;
      this.decls_value = null;
      this.decl_computed = false;
      this.decl_value = null;
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ConstructorAccess clone() throws CloneNotSupportedException {
      ConstructorAccess node = (ConstructorAccess)super.clone();
      node.decls_computed = false;
      node.decls_value = null;
      node.decl_computed = false;
      node.decl_value = null;
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ConstructorAccess copy() {
      try {
         ConstructorAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ConstructorAccess fullCopy() {
      ConstructorAccess tree = this.copy();
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

   public void exceptionHandling() {
      for(int i = 0; i < this.decl().getNumException(); ++i) {
         TypeDecl exceptionType = this.decl().getException(i).type();
         if (!this.handlesException(exceptionType)) {
            this.error("" + this + " may throw uncaught exception " + exceptionType.fullName());
         }
      }

   }

   protected boolean reachedException(TypeDecl catchType) {
      for(int i = 0; i < this.decl().getNumException(); ++i) {
         TypeDecl exceptionType = this.decl().getException(i).type();
         if (catchType.mayCatch(exceptionType)) {
            return true;
         }
      }

      return super.reachedException(catchType);
   }

   public void nameCheck() {
      super.nameCheck();
      if (this.decls().isEmpty()) {
         this.error("no constructor named " + this);
      }

      if (this.decls().size() > 1 && this.validArgs()) {
         this.error("several most specific constructors for " + this);
         Iterator iter = this.decls().iterator();

         while(iter.hasNext()) {
            this.error("         " + ((ConstructorDecl)iter.next()).signature());
         }
      }

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

   public void checkModifiers() {
      if (this.decl().isDeprecated() && !this.withinDeprecatedAnnotation() && this.hostType().topLevelType() != this.decl().hostType().topLevelType() && !this.withinSuppressWarnings("deprecation")) {
         this.warning(this.decl().signature() + " in " + this.decl().hostType().typeName() + " has been deprecated");
      }

   }

   protected void transformEnumConstructors() {
      super.transformEnumConstructors();
      this.getArgList().insertChild(new VarAccess("@p0"), 0);
      this.getArgList().insertChild(new VarAccess("@p1"), 1);
   }

   public void addEnclosingVariables() {
      if (this.addEnclosingVariables) {
         this.addEnclosingVariables = false;
         this.decl().addEnclosingVariables();
         Iterator iter = this.decl().hostType().enclosingVariables().iterator();

         while(iter.hasNext()) {
            Variable v = (Variable)iter.next();
            this.getArgList().add(new VarAccess("val$" + v.name()));
         }

      }
   }

   public void refined_Transformations_ConstructorAccess_transformation() {
      this.addEnclosingVariables();
      if (this.decl().isPrivate() && this.decl().hostType() != this.hostType()) {
         this.decl().createAccessor();
      }

      super.transformation();
   }

   public void collectTypesToSignatures(Collection<Type> set) {
      super.collectTypesToSignatures(set);
      this.addDependencyIfNeeded(set, this.decl().erasedConstructor().hostType());
   }

   public ConstructorAccess() {
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public ConstructorAccess(String p0, List<Expr> p1) {
      this.setID(p0);
      this.setChild(p1, 0);
   }

   public ConstructorAccess(Symbol p0, List<Expr> p1) {
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

      this.refined_Transformations_ConstructorAccess_transformation();
   }

   public Value eval(Body b) {
      b.setLine(this);
      ConstructorDecl c = this.decl().erasedConstructor();
      Local base = b.emitThis(this.hostType());
      int index = 0;
      ArrayList list = new ArrayList();
      if (c.needsEnclosing()) {
         list.add(this.asImmediate(b, b.newParameterRef(this.hostType().enclosingType().getSootType(), index++, this)));
      }

      if (c.needsSuperEnclosing()) {
         TypeDecl superClass = ((ClassDecl)this.hostType()).superclass();
         list.add(this.asImmediate(b, b.newParameterRef(superClass.enclosingType().getSootType(), index++, this)));
      }

      for(int i = 0; i < this.getNumArg(); ++i) {
         list.add(this.asImmediate(b, this.getArg(i).type().emitCastTo(b, this.getArg(i), c.getParameter(i).type())));
      }

      if (this.decl().isPrivate() && this.decl().hostType() != this.hostType()) {
         list.add(this.asImmediate(b, NullConstant.v()));
         b.add(b.newInvokeStmt(b.newSpecialInvokeExpr(base, this.decl().erasedConstructor().createAccessor().sootRef(), (java.util.List)list, this), this));
         return base;
      } else {
         return b.newSpecialInvokeExpr(base, c.sootRef(), (java.util.List)list, this);
      }
   }

   public boolean isDAafter(Variable v) {
      ASTNode$State state = this.state();
      return this.decl().isDAafter(v);
   }

   public boolean isDUafter(Variable v) {
      ASTNode$State state = this.state();
      return this.decl().isDUafter(v);
   }

   public boolean applicableAndAccessible(ConstructorDecl decl) {
      ASTNode$State state = this.state();
      return decl.applicable(this.getArgList()) && decl.accessibleFrom(this.hostType());
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
      return this.chooseConstructor(this.lookupConstructor(), this.getArgList());
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

   public boolean validArgs() {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumArg(); ++i) {
         if (this.getArg(i).type().isUnknown()) {
            return false;
         }
      }

      return true;
   }

   public String name() {
      ASTNode$State state = this.state();
      return "this";
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

   public Collection lookupConstructor() {
      ASTNode$State state = this.state();
      Collection lookupConstructor_value = this.getParent().Define_Collection_lookupConstructor(this, (ASTNode)null);
      return lookupConstructor_value;
   }

   public ConstructorDecl unknownConstructor() {
      ASTNode$State state = this.state();
      ConstructorDecl unknownConstructor_value = this.getParent().Define_ConstructorDecl_unknownConstructor(this, (ASTNode)null);
      return unknownConstructor_value;
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

   public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
      if (caller == this.getArgListNoTransform()) {
         caller.getIndexOfChild(child);
         return true;
      } else {
         return this.getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
