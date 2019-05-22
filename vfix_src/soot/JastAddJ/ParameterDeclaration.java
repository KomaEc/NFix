package soot.JastAddJ;

import beaver.Symbol;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import soot.Local;

public class ParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, Variable {
   private ParameterDeclaration iterElem;
   public Local local;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected boolean type_computed;
   protected TypeDecl type_value;
   protected boolean sourceVariableDecl_computed;
   protected Variable sourceVariableDecl_value;
   protected boolean throwTypes_computed;
   protected Collection<TypeDecl> throwTypes_value;
   protected boolean localNum_computed;
   protected int localNum_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
      this.sourceVariableDecl_computed = false;
      this.sourceVariableDecl_value = null;
      this.throwTypes_computed = false;
      this.throwTypes_value = null;
      this.localNum_computed = false;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParameterDeclaration clone() throws CloneNotSupportedException {
      ParameterDeclaration node = (ParameterDeclaration)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.throwTypes_computed = false;
      node.throwTypes_value = null;
      node.localNum_computed = false;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParameterDeclaration copy() {
      try {
         ParameterDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParameterDeclaration fullCopy() {
      ParameterDeclaration tree = this.copy();
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

   public ParameterDeclaration(Access type, String name) {
      this(new Modifiers(new List()), type, name);
   }

   public ParameterDeclaration(TypeDecl type, String name) {
      this(new Modifiers(new List()), type.createQualifiedAccess(), name);
   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" " + this.name());
   }

   public void jimplify2(Body b) {
      b.setLine(this);
      this.local = b.newLocal(this.name(), this.type().getSootType());
      b.add(b.newIdentityStmt(this.local, b.newParameterRef(this.type().getSootType(), this.localNum(), this), this));
   }

   public ParameterDeclaration() {
      this.type_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ParameterDeclaration(Modifiers p0, Access p1, String p2) {
      this.type_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
   }

   public ParameterDeclaration(Modifiers p0, Access p1, Symbol p2) {
      this.type_computed = false;
      this.sourceVariableDecl_computed = false;
      this.throwTypes_computed = false;
      this.localNum_computed = false;
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
   }

   protected int numChildren() {
      return 2;
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

   public void nameCheck() {
      SimpleSet decls = this.outerScope().lookupVariable(this.name());
      Iterator iter = decls.iterator();

      while(iter.hasNext()) {
         Variable var = (Variable)iter.next();
         if (var instanceof VariableDeclaration) {
            VariableDeclaration decl = (VariableDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of parameter " + this.name());
            }
         } else if (var instanceof ParameterDeclaration) {
            ParameterDeclaration decl = (ParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of parameter " + this.name());
            }
         } else if (var instanceof CatchParameterDeclaration) {
            CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of parameter " + this.name());
            }
         }
      }

      if (!this.lookupVariable(this.name()).contains(this)) {
         this.error("duplicate declaration of parameter " + this.name());
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

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynthetic();
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getID() + "]";
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
      return this.getTypeAccess().type();
   }

   public boolean isParameter() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isClassVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isInstanceVariable() {
      ASTNode$State state = this.state();
      return false;
   }

   public boolean isLocalVariable() {
      ASTNode$State state = this.state();
      return false;
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
      return true;
   }

   public boolean isStatic() {
      ASTNode$State state = this.state();
      return false;
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public boolean hasInit() {
      ASTNode$State state = this.state();
      return false;
   }

   public Expr getInit() {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException();
   }

   public Constant constant() {
      ASTNode$State state = this.state();
      throw new UnsupportedOperationException();
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

   public boolean isVariableArity() {
      ASTNode$State state = this.state();
      return false;
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
      if (this.isCatchParam() && this.effectivelyFinal()) {
         return this.catchClause().caughtExceptions();
      } else {
         Collection<TypeDecl> tts = new LinkedList();
         tts.add(this.type());
         return tts;
      }
   }

   public boolean effectivelyFinal() {
      ASTNode$State state = this.state();
      return this.isFinal() || !this.inhModifiedInScope(this);
   }

   public ParameterDeclaration substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
      ASTNode$State state = this.state();
      return new ParameterDeclaration((Modifiers)this.getModifiers().cloneSubtree(), this.getTypeAccess().substituted(original, substitution), this.getID());
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

   public BodyDecl enclosingBodyDecl() {
      ASTNode$State state = this.state();
      BodyDecl enclosingBodyDecl_value = this.getParent().Define_BodyDecl_enclosingBodyDecl(this, (ASTNode)null);
      return enclosingBodyDecl_value;
   }

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public boolean isMethodParameter() {
      ASTNode$State state = this.state();
      boolean isMethodParameter_value = this.getParent().Define_boolean_isMethodParameter(this, (ASTNode)null);
      return isMethodParameter_value;
   }

   public boolean isConstructorParameter() {
      ASTNode$State state = this.state();
      boolean isConstructorParameter_value = this.getParent().Define_boolean_isConstructorParameter(this, (ASTNode)null);
      return isConstructorParameter_value;
   }

   public boolean isExceptionHandlerParameter() {
      ASTNode$State state = this.state();
      boolean isExceptionHandlerParameter_value = this.getParent().Define_boolean_isExceptionHandlerParameter(this, (ASTNode)null);
      return isExceptionHandlerParameter_value;
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

   public boolean inhModifiedInScope(Variable var) {
      ASTNode$State state = this.state();
      boolean inhModifiedInScope_Variable_value = this.getParent().Define_boolean_inhModifiedInScope(this, (ASTNode)null, var);
      return inhModifiedInScope_Variable_value;
   }

   public boolean isCatchParam() {
      ASTNode$State state = this.state();
      boolean isCatchParam_value = this.getParent().Define_boolean_isCatchParam(this, (ASTNode)null);
      return isCatchParam_value;
   }

   public CatchClause catchClause() {
      ASTNode$State state = this.state();
      CatchClause catchClause_value = this.getParent().Define_CatchClause_catchClause(this, (ASTNode)null);
      return catchClause_value;
   }

   public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
      return caller == this.getModifiersNoTransform() ? true : this.getParent().Define_boolean_mayBeFinal(this, caller);
   }

   public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
      return caller == this.getModifiersNoTransform() ? name.equals("PARAMETER") : this.getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      return caller == this.getTypeAccessNoTransform() ? NameType.TYPE_NAME : this.getParent().Define_NameType_nameType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
