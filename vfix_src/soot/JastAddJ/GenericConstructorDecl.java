package soot.JastAddJ;

import beaver.Symbol;

public class GenericConstructorDecl extends ConstructorDecl implements Cloneable {
   public GenericConstructorDecl original;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public GenericConstructorDecl clone() throws CloneNotSupportedException {
      GenericConstructorDecl node = (GenericConstructorDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public GenericConstructorDecl copy() {
      try {
         GenericConstructorDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public GenericConstructorDecl fullCopy() {
      GenericConstructorDecl tree = this.copy();
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
      s.append(this.indent());
      this.getModifiers().toString(s);
      s.append(" <");

      int i;
      for(i = 0; i < this.getNumTypeParameter(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.original().getTypeParameter(i).toString(s);
      }

      s.append("> ");
      s.append(this.getID() + "(");
      if (this.getNumParameter() > 0) {
         this.getParameter(0).toString(s);

         for(i = 1; i < this.getNumParameter(); ++i) {
            s.append(", ");
            this.getParameter(i).toString(s);
         }
      }

      s.append(")");
      if (this.getNumException() > 0) {
         s.append(" throws ");
         this.getException(0).toString(s);

         for(i = 1; i < this.getNumException(); ++i) {
            s.append(", ");
            this.getException(i).toString(s);
         }
      }

      s.append(" {");
      if (this.hasConstructorInvocation()) {
         s.append(this.indent());
         this.getConstructorInvocation().toString(s);
      }

      for(i = 0; i < this.getBlock().getNumStmt(); ++i) {
         s.append(this.indent());
         this.getBlock().getStmt(i).toString(s);
      }

      s.append(this.indent());
      s.append("}");
   }

   public GenericConstructorDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
      this.setChild(new List(), 5);
   }

   public GenericConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<TypeVariable> p6) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   public GenericConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<TypeVariable> p6) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   protected int numChildren() {
      return 6;
   }

   public boolean mayHaveRewrite() {
      return true;
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

   public void setParameterList(List<ParameterDeclaration> list) {
      this.setChild(list, 1);
   }

   public int getNumParameter() {
      return this.getParameterList().getNumChild();
   }

   public int getNumParameterNoTransform() {
      return this.getParameterListNoTransform().getNumChildNoTransform();
   }

   public ParameterDeclaration getParameter(int i) {
      return (ParameterDeclaration)this.getParameterList().getChild(i);
   }

   public void addParameter(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.parent != null && state != null ? this.getParameterList() : this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void addParameterNoTransform(ParameterDeclaration node) {
      List<ParameterDeclaration> list = this.getParameterListNoTransform();
      list.addChild(node);
   }

   public void setParameter(ParameterDeclaration node, int i) {
      List<ParameterDeclaration> list = this.getParameterList();
      list.setChild(node, i);
   }

   public List<ParameterDeclaration> getParameters() {
      return this.getParameterList();
   }

   public List<ParameterDeclaration> getParametersNoTransform() {
      return this.getParameterListNoTransform();
   }

   public List<ParameterDeclaration> getParameterList() {
      List<ParameterDeclaration> list = (List)this.getChild(1);
      list.getNumChild();
      return list;
   }

   public List<ParameterDeclaration> getParameterListNoTransform() {
      return (List)this.getChildNoTransform(1);
   }

   public void setExceptionList(List<Access> list) {
      this.setChild(list, 2);
   }

   public int getNumException() {
      return this.getExceptionList().getNumChild();
   }

   public int getNumExceptionNoTransform() {
      return this.getExceptionListNoTransform().getNumChildNoTransform();
   }

   public Access getException(int i) {
      return (Access)this.getExceptionList().getChild(i);
   }

   public void addException(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getExceptionList() : this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void addExceptionNoTransform(Access node) {
      List<Access> list = this.getExceptionListNoTransform();
      list.addChild(node);
   }

   public void setException(Access node, int i) {
      List<Access> list = this.getExceptionList();
      list.setChild(node, i);
   }

   public List<Access> getExceptions() {
      return this.getExceptionList();
   }

   public List<Access> getExceptionsNoTransform() {
      return this.getExceptionListNoTransform();
   }

   public List<Access> getExceptionList() {
      List<Access> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<Access> getExceptionListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setConstructorInvocationOpt(Opt<Stmt> opt) {
      this.setChild(opt, 3);
   }

   public boolean hasConstructorInvocation() {
      return this.getConstructorInvocationOpt().getNumChild() != 0;
   }

   public Stmt getConstructorInvocation() {
      return (Stmt)this.getConstructorInvocationOpt().getChild(0);
   }

   public void setConstructorInvocation(Stmt node) {
      this.getConstructorInvocationOpt().setChild(node, 0);
   }

   public Opt<Stmt> getConstructorInvocationOpt() {
      return (Opt)this.getChild(3);
   }

   public Opt<Stmt> getConstructorInvocationOptNoTransform() {
      return (Opt)this.getChildNoTransform(3);
   }

   public void setBlock(Block node) {
      this.setChild(node, 4);
   }

   public Block getBlock() {
      return (Block)this.getChild(4);
   }

   public Block getBlockNoTransform() {
      return (Block)this.getChildNoTransform(4);
   }

   public void setTypeParameterList(List<TypeVariable> list) {
      this.setChild(list, 5);
   }

   public int getNumTypeParameter() {
      return this.getTypeParameterList().getNumChild();
   }

   public int getNumTypeParameterNoTransform() {
      return this.getTypeParameterListNoTransform().getNumChildNoTransform();
   }

   public TypeVariable getTypeParameter(int i) {
      return (TypeVariable)this.getTypeParameterList().getChild(i);
   }

   public void addTypeParameter(TypeVariable node) {
      List<TypeVariable> list = this.parent != null && state != null ? this.getTypeParameterList() : this.getTypeParameterListNoTransform();
      list.addChild(node);
   }

   public void addTypeParameterNoTransform(TypeVariable node) {
      List<TypeVariable> list = this.getTypeParameterListNoTransform();
      list.addChild(node);
   }

   public void setTypeParameter(TypeVariable node, int i) {
      List<TypeVariable> list = this.getTypeParameterList();
      list.setChild(node, i);
   }

   public List<TypeVariable> getTypeParameters() {
      return this.getTypeParameterList();
   }

   public List<TypeVariable> getTypeParametersNoTransform() {
      return this.getTypeParameterListNoTransform();
   }

   public List<TypeVariable> getTypeParameterList() {
      List<TypeVariable> list = (List)this.getChild(5);
      list.getNumChild();
      return list;
   }

   public List<TypeVariable> getTypeParameterListNoTransform() {
      return (List)this.getChildNoTransform(5);
   }

   public SimpleSet localLookupType(String name) {
      ASTNode$State state = this.state();

      for(int i = 0; i < this.getNumTypeParameter(); ++i) {
         if (this.original().getTypeParameter(i).name().equals(name)) {
            return SimpleSet.emptySet.add(this.original().getTypeParameter(i));
         }
      }

      return SimpleSet.emptySet;
   }

   public GenericConstructorDecl original() {
      ASTNode$State state = this.state();
      return this.original != null ? this.original : this;
   }

   public SimpleSet lookupType(String name) {
      ASTNode$State state = this.state();
      SimpleSet lookupType_String_value = this.getParent().Define_SimpleSet_lookupType(this, (ASTNode)null, name);
      return lookupType_String_value;
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeParameterListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      this.getIndexOfChild(caller);
      return this.localLookupType(name).isEmpty() ? this.lookupType(name) : this.localLookupType(name);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
