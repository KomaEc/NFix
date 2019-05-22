package soot.JastAddJ;

import beaver.Symbol;

public class VariableDecl extends ASTNode<ASTNode> implements Cloneable {
   protected String tokenString_ID;
   public int IDstart;
   public int IDend;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VariableDecl clone() throws CloneNotSupportedException {
      VariableDecl node = (VariableDecl)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VariableDecl copy() {
      try {
         VariableDecl node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VariableDecl fullCopy() {
      VariableDecl tree = this.copy();
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

   public VariableDeclaration createVariableDeclarationFrom(Modifiers modifiers, Access type) {
      VariableDeclaration decl = new VariableDeclaration(modifiers, type.addArrayDims(this.getDimsList()), this.getID(), this.getInitOpt());
      decl.setStart(this.start);
      decl.setEnd(this.end);
      decl.IDstart = this.IDstart;
      decl.IDend = this.IDend;
      return decl;
   }

   public FieldDeclaration createFieldDeclarationFrom(Modifiers modifiers, Access type) {
      FieldDeclaration decl = new FieldDeclaration(modifiers, type.addArrayDims(this.getDimsList()), this.getID(), this.getInitOpt());
      decl.setStart(this.start);
      decl.setEnd(this.end);
      decl.IDstart = this.IDstart;
      decl.IDend = this.IDend;
      return decl;
   }

   public VariableDecl() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
      this.setChild(new List(), 0);
      this.setChild(new Opt(), 1);
   }

   public VariableDecl(String p0, List<Dims> p1, Opt<Expr> p2) {
      this.setID(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   public VariableDecl(Symbol p0, List<Dims> p1, Opt<Expr> p2) {
      this.setID(p0);
      this.setChild(p1, 0);
      this.setChild(p2, 1);
   }

   protected int numChildren() {
      return 2;
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

   public void setDimsList(List<Dims> list) {
      this.setChild(list, 0);
   }

   public int getNumDims() {
      return this.getDimsList().getNumChild();
   }

   public int getNumDimsNoTransform() {
      return this.getDimsListNoTransform().getNumChildNoTransform();
   }

   public Dims getDims(int i) {
      return (Dims)this.getDimsList().getChild(i);
   }

   public void addDims(Dims node) {
      List<Dims> list = this.parent != null && state != null ? this.getDimsList() : this.getDimsListNoTransform();
      list.addChild(node);
   }

   public void addDimsNoTransform(Dims node) {
      List<Dims> list = this.getDimsListNoTransform();
      list.addChild(node);
   }

   public void setDims(Dims node, int i) {
      List<Dims> list = this.getDimsList();
      list.setChild(node, i);
   }

   public List<Dims> getDimss() {
      return this.getDimsList();
   }

   public List<Dims> getDimssNoTransform() {
      return this.getDimsListNoTransform();
   }

   public List<Dims> getDimsList() {
      List<Dims> list = (List)this.getChild(0);
      list.getNumChild();
      return list;
   }

   public List<Dims> getDimsListNoTransform() {
      return (List)this.getChildNoTransform(0);
   }

   public void setInitOpt(Opt<Expr> opt) {
      this.setChild(opt, 1);
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
      return (Opt)this.getChild(1);
   }

   public Opt<Expr> getInitOptNoTransform() {
      return (Opt)this.getChildNoTransform(1);
   }

   public String name() {
      ASTNode$State state = this.state();
      return this.getID();
   }

   public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? true : this.getParent().Define_boolean_isSource(this, caller);
   }

   public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
      return caller == this.getInitOptNoTransform() ? null : this.getParent().Define_TypeDecl_expectedType(this, caller);
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
