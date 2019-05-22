package soot.JastAddJ;

import beaver.Symbol;

public class ConstructorDeclSubstituted extends ConstructorDecl implements Cloneable {
   protected ConstructorDecl tokenConstructorDecl_Original;
   protected boolean sourceConstructorDecl_computed = false;
   protected ConstructorDecl sourceConstructorDecl_value;

   public void flushCache() {
      super.flushCache();
      this.sourceConstructorDecl_computed = false;
      this.sourceConstructorDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ConstructorDeclSubstituted clone() throws CloneNotSupportedException {
      ConstructorDeclSubstituted node = (ConstructorDeclSubstituted)super.clone();
      node.sourceConstructorDecl_computed = false;
      node.sourceConstructorDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ConstructorDeclSubstituted copy() {
      try {
         ConstructorDeclSubstituted node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ConstructorDeclSubstituted fullCopy() {
      ConstructorDeclSubstituted tree = this.copy();
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

   public ConstructorDecl createAccessor() {
      return this.erasedConstructor().createAccessor();
   }

   protected TypeDecl createAnonymousJavaTypeDecl() {
      return this.erasedConstructor().createAnonymousJavaTypeDecl();
   }

   public ConstructorDeclSubstituted() {
   }

   public void init$Children() {
      this.children = new ASTNode[5];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
   }

   public ConstructorDeclSubstituted(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, ConstructorDecl p6) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setOriginal(p6);
   }

   public ConstructorDeclSubstituted(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, ConstructorDecl p6) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setOriginal(p6);
   }

   protected int numChildren() {
      return 5;
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

   public void setOriginal(ConstructorDecl value) {
      this.tokenConstructorDecl_Original = value;
   }

   public ConstructorDecl getOriginal() {
      return this.tokenConstructorDecl_Original;
   }

   public ConstructorDecl sourceConstructorDecl() {
      if (this.sourceConstructorDecl_computed) {
         return this.sourceConstructorDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.sourceConstructorDecl_value = this.sourceConstructorDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.sourceConstructorDecl_computed = true;
         }

         return this.sourceConstructorDecl_value;
      }
   }

   private ConstructorDecl sourceConstructorDecl_compute() {
      return this.getOriginal().sourceConstructorDecl();
   }

   public ConstructorDecl erasedConstructor() {
      ASTNode$State state = this.state();
      return this.getOriginal().erasedConstructor();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
