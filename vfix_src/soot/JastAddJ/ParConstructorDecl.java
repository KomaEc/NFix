package soot.JastAddJ;

import beaver.Symbol;

public class ParConstructorDecl extends ConstructorDecl implements Cloneable {
   protected boolean genericConstructorDecl_computed = false;
   protected GenericConstructorDecl genericConstructorDecl_value;
   protected boolean sourceConstructorDecl_computed = false;
   protected ConstructorDecl sourceConstructorDecl_value;

   public void flushCache() {
      super.flushCache();
      this.genericConstructorDecl_computed = false;
      this.genericConstructorDecl_value = null;
      this.sourceConstructorDecl_computed = false;
      this.sourceConstructorDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParConstructorDecl clone() throws CloneNotSupportedException {
      ParConstructorDecl node = (ParConstructorDecl)super.clone();
      node.genericConstructorDecl_computed = false;
      node.genericConstructorDecl_value = null;
      node.sourceConstructorDecl_computed = false;
      node.sourceConstructorDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParConstructorDecl copy() {
      try {
         ParConstructorDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParConstructorDecl fullCopy() {
      ParConstructorDecl tree = this.copy();
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

   public ParConstructorDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 1);
      this.setChild(new List(), 2);
      this.setChild(new Opt(), 3);
      this.setChild(new List(), 5);
   }

   public ParConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
      this.setChild(p0, 0);
      this.setID(p1);
      this.setChild(p2, 1);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
   }

   public ParConstructorDecl(Modifiers p0, Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5, List<Access> p6) {
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

   public void setTypeArgumentList(List<Access> list) {
      this.setChild(list, 5);
   }

   public int getNumTypeArgument() {
      return this.getTypeArgumentList().getNumChild();
   }

   public int getNumTypeArgumentNoTransform() {
      return this.getTypeArgumentListNoTransform().getNumChildNoTransform();
   }

   public Access getTypeArgument(int i) {
      return (Access)this.getTypeArgumentList().getChild(i);
   }

   public void addTypeArgument(Access node) {
      List<Access> list = this.parent != null && state != null ? this.getTypeArgumentList() : this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void addTypeArgumentNoTransform(Access node) {
      List<Access> list = this.getTypeArgumentListNoTransform();
      list.addChild(node);
   }

   public void setTypeArgument(Access node, int i) {
      List<Access> list = this.getTypeArgumentList();
      list.setChild(node, i);
   }

   public List<Access> getTypeArguments() {
      return this.getTypeArgumentList();
   }

   public List<Access> getTypeArgumentsNoTransform() {
      return this.getTypeArgumentListNoTransform();
   }

   public List<Access> getTypeArgumentList() {
      List<Access> list = (List)this.getChild(5);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeArgumentListNoTransform() {
      return (List)this.getChildNoTransform(5);
   }

   public GenericConstructorDecl genericConstructorDecl() {
      if (this.genericConstructorDecl_computed) {
         return this.genericConstructorDecl_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.genericConstructorDecl_value = this.genericConstructorDecl_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.genericConstructorDecl_computed = true;
         }

         return this.genericConstructorDecl_value;
      }
   }

   private GenericConstructorDecl genericConstructorDecl_compute() {
      return this.getParent() != null && this.getParent().getParent() instanceof GenericConstructorDecl ? (GenericConstructorDecl)this.getParent().getParent() : null;
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
      return this.genericConstructorDecl().original().sourceConstructorDecl();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
