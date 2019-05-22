package soot.JastAddJ;

import beaver.Symbol;

public class RawMethodDecl extends ParMethodDecl implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public RawMethodDecl clone() throws CloneNotSupportedException {
      RawMethodDecl node = (RawMethodDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public RawMethodDecl copy() {
      try {
         RawMethodDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public RawMethodDecl fullCopy() {
      RawMethodDecl tree = this.copy();
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

   public boolean isRawType() {
      return true;
   }

   public RawMethodDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[6];
      this.setChild(new List(), 2);
      this.setChild(new List(), 3);
      this.setChild(new Opt(), 4);
      this.setChild(new List(), 5);
   }

   public RawMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
      this.setGenericMethodDecl(p7);
   }

   public RawMethodDecl(Modifiers p0, Access p1, Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<Access> p6, GenericMethodDecl p7) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setChild(p4, 3);
      this.setChild(p5, 4);
      this.setChild(p6, 5);
      this.setGenericMethodDecl(p7);
   }

   protected int numChildren() {
      return 6;
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

   public void setParameterList(List<ParameterDeclaration> list) {
      this.setChild(list, 2);
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
      List<ParameterDeclaration> list = (List)this.getChild(2);
      list.getNumChild();
      return list;
   }

   public List<ParameterDeclaration> getParameterListNoTransform() {
      return (List)this.getChildNoTransform(2);
   }

   public void setExceptionList(List<Access> list) {
      this.setChild(list, 3);
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
      List<Access> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<Access> getExceptionListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   public void setBlockOpt(Opt<Block> opt) {
      this.setChild(opt, 4);
   }

   public boolean hasBlock() {
      return this.getBlockOpt().getNumChild() != 0;
   }

   public Block getBlock() {
      return (Block)this.getBlockOpt().getChild(0);
   }

   public void setBlock(Block node) {
      this.getBlockOpt().setChild(node, 0);
   }

   public Opt<Block> getBlockOpt() {
      return (Opt)this.getChild(4);
   }

   public Opt<Block> getBlockOptNoTransform() {
      return (Opt)this.getChildNoTransform(4);
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

   public void setGenericMethodDecl(GenericMethodDecl value) {
      this.tokenGenericMethodDecl_GenericMethodDecl = value;
   }

   public GenericMethodDecl getGenericMethodDecl() {
      return this.tokenGenericMethodDecl_GenericMethodDecl;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
