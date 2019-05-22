package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CatchParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, Variable, SimpleSet, Iterator {
   private CatchParameterDeclaration iterElem;
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;
   protected boolean sourceVariableDecl_computed = false;
   protected Variable sourceVariableDecl_value;
   protected boolean throwTypes_computed = false;
   protected Collection<TypeDecl> throwTypes_value;

   public void flushCache() {
      super.flushCache();
      this.sourceVariableDecl_computed = false;
      this.sourceVariableDecl_value = null;
      this.throwTypes_computed = false;
      this.throwTypes_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public CatchParameterDeclaration clone() throws CloneNotSupportedException {
      CatchParameterDeclaration node = (CatchParameterDeclaration)super.clone();
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.throwTypes_computed = false;
      node.throwTypes_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public CatchParameterDeclaration copy() {
      try {
         CatchParameterDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public CatchParameterDeclaration fullCopy() {
      CatchParameterDeclaration tree = this.copy();
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

   public void typeCheck() {
      boolean pass = true;

      for(int i = 0; i < this.getNumTypeAccess(); ++i) {
         for(int j = 0; j < this.getNumTypeAccess(); ++j) {
            if (i != j) {
               TypeDecl t1 = this.getTypeAccess(i).type();
               TypeDecl t2 = this.getTypeAccess(j).type();
               if (t2.instanceOf(t1)) {
                  this.error(t2.fullName() + " is a subclass of " + t1.fullName());
                  pass = false;
               }
            }
         }
      }

   }

   public void toString(StringBuffer sb) {
      this.getModifiers().toString(sb);

      for(int i = 0; i < this.getNumTypeAccess(); ++i) {
         if (i > 0) {
            sb.append(" | ");
         }

         this.getTypeAccess(i).toString(sb);
      }

      sb.append(" " + this.getID());
   }

   public void nameCheck() {
      SimpleSet decls = this.outerScope().lookupVariable(this.name());
      Iterator iter = decls.iterator();

      while(iter.hasNext()) {
         Variable var = (Variable)iter.next();
         if (var instanceof VariableDeclaration) {
            VariableDeclaration decl = (VariableDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of catch parameter " + this.name());
            }
         } else if (var instanceof ParameterDeclaration) {
            ParameterDeclaration decl = (ParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of catch parameter " + this.name());
            }
         } else if (var instanceof CatchParameterDeclaration) {
            CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
            if (decl.enclosingBodyDecl() == this.enclosingBodyDecl()) {
               this.error("duplicate declaration of catch parameter " + this.name());
            }
         }
      }

      if (!this.lookupVariable(this.name()).contains(this)) {
         this.error("duplicate declaration of catch parameter " + this.name());
      }

   }

   public CatchParameterDeclaration() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 1);
   }

   public CatchParameterDeclaration(Modifiers p0, List<Access> p1, String p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
   }

   public CatchParameterDeclaration(Modifiers p0, List<Access> p1, Symbol p2) {
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

   public void setTypeAccessList(List<Access> list) {
      this.setChild(list, 1);
   }

   public int getNumTypeAccess() {
      return this.getTypeAccessList().getNumChild();
   }

   public int getNumTypeAccessNoTransform() {
      return this.getTypeAccessListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeAccess(int i) {
      return (Access)this.getTypeAccessList().getChild(i);
   }

   public void addTypeAccess(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeAccessList() : this.getTypeAccessListNoTransform();
      list.addChild(node);
   }

   public void addTypeAccessNoTransform(Access node) {
      List<Access> list = this.getTypeAccessListNoTransform();
      list.addChild(node);
   }

   public void setTypeAccess(Access node, int i) {
      List<Access> list = this.getTypeAccessList();
      list.setChild(node, i);
   }

   public List<Access> getTypeAccesss() {
      return this.getTypeAccessList();
   }

   public List<Access> getTypeAccesssNoTransform() {
      return this.getTypeAccessListNoTransform();
   }

   public List<Access> getTypeAccessList() {
      List<Access> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeAccessListNoTransform() {
      return (List)this.getChildNoTransform(1);
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
      return true;
   }

   public boolean isVolatile() {
      ASTNode$State state = this.state();
      return this.getModifiers().isVolatile();
   }

   public boolean isBlank() {
      ASTNode$State state = this.state();
      return false;
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

   public boolean isSynthetic() {
      ASTNode$State state = this.state();
      return this.getModifiers().isSynthetic();
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

   public TypeDecl type() {
      ASTNode$State state = this.state();
      ArrayList<TypeDecl> list = new ArrayList();

      for(int i = 0; i < this.getNumTypeAccess(); ++i) {
         list.add(this.getTypeAccess(i).type());
      }

      return this.lookupLUBType(list).lub();
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
      return this.catchClause().caughtExceptions();
   }

   public SimpleSet lookupVariable(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupVariable_String_value = this.getParent().Define_SimpleSet_lookupVariable(this, (ASTNode)null, name);
      return lookupVariable_String_value;
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

   public TypeDecl hostType() {
      ASTNode$State state = this.state();
      TypeDecl hostType_value = this.getParent().Define_TypeDecl_hostType(this, (ASTNode)null);
      return hostType_value;
   }

   public LUBType lookupLUBType(Collection bounds) {
      ASTNode$State state = this.state();
      LUBType lookupLUBType_Collection_value = this.getParent().Define_LUBType_lookupLUBType(this, (ASTNode)null, bounds);
      return lookupLUBType_Collection_value;
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

   public CatchClause catchClause() {
      ASTNode$State state = this.state();
      CatchClause catchClause_value = this.getParent().Define_CatchClause_catchClause(this, (ASTNode)null);
      return catchClause_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeAccessListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return this.getParent().Define_NameType_nameType(this, caller);
      }
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
