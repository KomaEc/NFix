package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.Local;

public class VariableDeclaration extends Stmt implements Cloneable, SimpleSet, Iterator, Variable {
   private VariableDeclaration iterElem;
   public Local local;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected Map isDAafter_Variable_values;
   protected Map isDUafter_Variable_values;
   protected boolean constant_computed;
   protected Constant constant_value;
   protected boolean sourceVariableDecl_computed;
   protected Variable sourceVariableDecl_value;
   protected boolean throwTypes_computed;
   protected Collection<TypeDecl> throwTypes_value;
   protected boolean localNum_computed;
   protected int localNum_value;

   public void flushCache() {
      super.flushCache();
      this.isDAafter_Variable_values = null;
      this.isDUafter_Variable_values = null;
      this.constant_computed = false;
      this.constant_value = null;
      this.sourceVariableDecl_computed = false;
      this.sourceVariableDecl_value = null;
      this.throwTypes_computed = false;
      this.throwTypes_value = null;
      this.localNum_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VariableDeclaration clone() throws CloneNotSupportedException {
      VariableDeclaration node = (VariableDeclaration)super.clone();
      node.isDAafter_Variable_values = null;
      node.isDUafter_Variable_values = null;
      node.constant_computed = false;
      node.constant_value = null;
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.throwTypes_computed = false;
      node.throwTypes_value = null;
      node.localNum_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VariableDeclaration copy() {
      try {
         VariableDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VariableDeclaration fullCopy() {
      VariableDeclaration tree = this.copy();
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

   public SimpleSet add(Object o) {
      return (new SimpleSet.SimpleSetImpl()).add(this).add(o);
   }

   public boolean isSingleton() {
      return true;
   }

   public boolean isSingleton(Object o) {
      return this.contains(o);
   }

   public Iterator iterator() {
      this.iterElem = this;
      return this;
   }

   public boolean hasNext() {
      return this.iterElem != null;
   }

   public Object next() {
      Object o = this.iterElem;
      this.iterElem = null;
      return o;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public VariableDeclaration(Access type, String name, Expr init) {
      this(new Modifiers(new List()), type, name, new Opt(init));
   }

   public VariableDeclaration(Access type, String name) {
      this(new Modifiers(new List()), type, name, new Opt());
   }

   public void toString(StringBuffer s) {
      s.append(this.indent());
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.name());
      if (this.hasInit()) {
         s.append(" = ");
         this.getInit().toString(s);
      }

      s.append(";");
   }

   public void typeCheck() {
      if (this.hasInit()) {
         TypeDecl source = this.getInit().type();
         TypeDecl dest = this.type();
         if (!source.assignConversionTo(dest, this.getInit())) {
            this.error("can not assign variable " + this.name() + " of type " + dest.typeName() + " a value of type " + source.typeName());
         }
      }

   }

   public void jimplify2(Body b) {
      b.setLine(this);
      this.local = b.newLocal(this.name(), this.type().getSootType());
      if (this.hasInit()) {
         b.add(b.newAssignStmt(this.local, this.asRValue(b, this.getInit().type().emitCastTo(b, this.getInit(), this.type())), this));
      }

   }

   public void checkWarnings() {
      if (this.hasInit() && !this.suppressWarnings("unchecked")) {
         this.checkUncheckedConversion(this.getInit().type(), this.type());
      }

   }

   public VariableDeclaration() {
      this.constant_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 2);
   }

   public VariableDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
      this.constant_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   public VariableDeclaration(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3) {
      this.constant_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
   }

   protected int numChildren() {
      return 3;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setModifiers(Modifiers node) {
      this.setChild(node, 0);
   }

   public Modifiers getModifiers() {
      return (Modifiers)this.getChild(0);
   }

   public Modifiers getModifiersNoTransform() {
      return (Modifiers)this.getChildNoTransform(0);
   }

   public void setTypeAccess(Access node) {
      this.setChild(node, 1);
   }

   public Access getTypeAccess() {
      return (Access)this.getChild(1);
   }

   public Access getTypeAccessNoTransform() {
      return (Access)this.getChildNoTransform(1);
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

   public void setInitOpt(Opt<Expr> opt) {
      this.setChild(opt, 2);
   }

   public boolean hasInit() {
      return this.getInitOpt().getNumChild() != 0;
   }

   public Expr getInit() {
      return (Expr)this.getInitOpt().getChild(0);
   }

   public void setInit(Expr node) {
      this.getInitOpt().setChild(node, 0);
   }

   public Opt<Expr> getInitOpt() {
      return (Opt)this.getChild(2);
   }

   public Opt<Expr> getInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(2);
   }

   public void nameCheck() {
      SimpleSet decls = this.outerScope().lookupVariable(this.name());
      Iterator iter = decls.iterator();

      while(iter.hasNext()) {
         Variable var = (Variable)iter.next();
         if (var instanceof VariableDeclaration) {
            VariableDeclaration decl = (VariableDeclaration)var;
            if (decl != this && decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of local variable " + this.name());
            }
         } else if (var instanceof ParameterDeclaration) {
            ParameterDeclaration decl = (ParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of local variable " + this.name());
            }
         } else if (var instanceof CatchParameterDeclaration) {
            CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of local variable " + this.name());
            }
         }
      }

      if (this.getParent().getParent() instanceof Block) {
         Block block = (Block)this.getParent().getParent();

         for(int i = 0; i < block.getNumStmt(); ++i) {
            if (block.getStmt(i) instanceof Variable) {
               Variable v = (Variable)block.getStmt(i);
               if (v.name().equals(this.name()) && v != this) {
                  this.error("duplicate declaration of local variable " + this.name());
               }
            }
         }
      }

   }

   public int size() {
      ASTNode$State state = this.state();
      return 1;
   }

   public boolean isEmpty() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean contains(Object o) {
      ASTNode$State state = this.state();
      return this == o;
   }

   public boolean isBlankFinal() {
      ASTNode$State state = this.state();
      return this.isFinal() && (!this.hasInit() || !this.getInit().isConstant());
   }

   public boolean isValue() {
      ASTNode$State state = this.state();
      return this.isFinal() && this.hasInit() && this.getInit().isConstant();
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
      if (v == this) {
         return this.hasInit();
      } else {
         return this.hasInit() ? this.getInit().isDAafter(v) : this.isDAbefore(v);
      }
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
      if (v == this) {
         return !this.hasInit();
      } else {
         return this.hasInit() ? this.getInit().isDUafter(v) : this.isDUbefore(v);
      }
   }

   public boolean declaresVariable(String name) {
      ASTNode$State state = this.state();
      return this.name().equals(name);
   }

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynthetic();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
   }

   public TypeDecl type() {
      ASTNode$State state = this.state();
      return this.getTypeAccess().type();
   }

   public boolean isParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isClassVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isInstanceVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isMethodParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isConstructorParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isExceptionHandlerParameter() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isLocalVariable() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isFinal() {
      ASTNode$State state = this.state();
      return this.getModifiers().isFinal();
   }

   public boolean isVolatile() {
      ASTNode$State state = this.state();
      return this.getModifiers().isVolatile();
   }

   public boolean isBlank() {
      ASTNode$State state = this.state();
      return !this.hasInit();
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return false;
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public Constant constant() {
      if (this.constant_computed) {
         return this.constant_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.constant_value = this.constant_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.constant_computed = true;
         }

         return this.constant_value;
      }
   }

   private Constant constant_compute() {
      return this.type().cast(this.getInit().constant());
   }

   public Variable sourceVariableDecl() {
      if (this.sourceVariableDecl_computed) {
         return this.sourceVariableDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceVariableDecl_value = this.sourceVariableDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceVariableDecl_computed = true;
         }

         return this.sourceVariableDecl_value;
      }
   }

   private Variable sourceVariableDecl_compute() {
      return this;
   }

   public Collection<TypeDecl> throwTypes() {
      if (this.throwTypes_computed) {
         return this.throwTypes_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.throwTypes_value = this.throwTypes_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.throwTypes_computed = true;
         }

         return this.throwTypes_value;
      }
   }

   private Collection<TypeDecl> throwTypes_compute() {
      Collection<TypeDecl> tts = new LinkedList();
      tts.add(this.type());
      return tts;
   }

   public boolean modifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean hasAnnotationSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      return this.getModifiers().hasAnnotationSuppressWarnings(s);
   }

   public boolean suppressWarnings(String type) {
      ASTNode$State state = this.state();
      return this.hasAnnotationSuppressWarnings(type) || this.withinSuppressWarnings(type);
   }

   public SimpleSet lookupVariable(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
   }

   public VariableScope outerScope() {
      ASTNode$State state = this.state();
      VariableScope outerScope_value = this.getParent().Define_VariableScope_outerScope(this, (ASTNode)null);
      return outerScope_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public int localNum() {
      if (this.localNum_computed) {
         return this.localNum_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.localNum_value = this.getParent().Define_int_localNum(this, (ASTNode)null);
         if (isFinal && num == this.state().boundariesCrossed) {
            this.localNum_computed = true;
         }

         return this.localNum_value;
      }
   }

   public boolean withinSuppressWarnings(String s) {
      ASTNode$State state = this.state();
      boolean withinSuppressWarnings_String_value = this.getParent().Define_boolean_withinSuppressWarnings(this, (ASTNode)null, s);
      return withinSuppressWarnings_String_value;
   }

   public boolean resourcePreviouslyDeclared(String name) {
      ASTNode$State state = this.state();
      boolean resourcePreviouslyDeclared_String_value = this.getParent().Define_boolean_resourcePreviouslyDeclared(this, (ASTNode)null, name);
      return resourcePreviouslyDeclared_String_value;
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getInitOptNoTransform() ? this.isDAbefore(v) : this.getParent().Define_boolean_isDAbefore(this, caller, v);
   }

   public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
      return caller == this.getInitOptNoTransform() ? this.isDUbefore(v) : this.getParent().Define_boolean_isDUbefore(this, caller, v);
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeFinal(this, caller);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_declType(this, caller);
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("LOCAL_VARIABLE") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type() : this.getParent().Define_TypeDecl_assignConvertedType(this, caller);
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? this.type().componentType() : this.getParent().Define_TypeDecl_expectedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
