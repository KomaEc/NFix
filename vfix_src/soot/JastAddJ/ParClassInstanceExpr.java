package soot.JastAddJ;

public class ParClassInstanceExpr extends ClassInstanceExpr implements Cloneable {
   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public ParClassInstanceExpr clone() throws CloneNotSupportedException {
      ParClassInstanceExpr node = (ParClassInstanceExpr)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParClassInstanceExpr copy() {
      try {
         ParClassInstanceExpr node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParClassInstanceExpr fullCopy() {
      ParClassInstanceExpr tree = this.copy();
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
      s.append("<");

      for(int i = 0; i < this.getNumTypeArgument(); ++i) {
         if (i != 0) {
            s.append(", ");
         }

         this.getTypeArgument(i).toString(s);
      }

      s.append(">");
      super.toString(s);
   }

   public ParClassInstanceExpr() {
   }

   public void init$Children() {
      this.children = new ASTNode[4];
      this.setChild(new List(), 1);
      this.setChild(new Opt(), 2);
      this.setChild(new List(), 3);
   }

   public ParClassInstanceExpr(Access p0, List<Expr> p1, Opt<TypeDecl> p2, List<Access> p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setChild(p2, 2);
      this.setChild(p3, 3);
   }

   protected int numChildren() {
      return 4;
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

   public void setTypeArgumentList(List<Access> list) {
      this.setChild(list, 3);
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
      List<Access> list = (List)this.getChild(3);
      list.getNumChild();
      return list;
   }

   public List<Access> getTypeArgumentListNoTransform() {
      return (List)this.getChildNoTransform(3);
   }

   public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
      if (caller == this.getTypeArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return NameType.TYPE_NAME;
      } else {
         return super.Define_NameType_nameType(caller, child);
      }
   }

   public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
      if (caller == this.getTypeArgumentListNoTransform()) {
         caller.getIndexOfChild(child);
         return this.unqualifiedScope().lookupType(name);
      } else {
         return super.Define_SimpleSet_lookupType(caller, child, name);
      }
   }

   public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
      return caller == this.getAccessNoTransform() ? true : this.getParent().Define_boolean_isExplicitGenericConstructorAccess(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
