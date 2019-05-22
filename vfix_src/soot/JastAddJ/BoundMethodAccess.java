package soot.JastAddJ;

import beaver.Symbol;

public class BoundMethodAccess extends MethodAccess implements Cloneable {
   private MethodDecl methodDecl;
   protected boolean decl_computed;
   protected MethodDecl decl_value;

   public void flushCache() {
      super.flushCache();
      this.decl_computed = false;
      this.decl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public BoundMethodAccess clone() throws CloneNotSupportedException {
      BoundMethodAccess node = (BoundMethodAccess)super.clone();
      node.decl_computed = false;
      node.decl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public BoundMethodAccess copy() {
      try {
         BoundMethodAccess node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public BoundMethodAccess fullCopy() {
      BoundMethodAccess tree = this.copy();
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

   public BoundMethodAccess(String name, List args, MethodDecl methodDecl) {
      this(name, args);
      this.methodDecl = methodDecl;
   }

   public BoundMethodAccess() {
      this.decl_computed = false;
   }

   public void init$Children() {
      this.children = new ASTNode[1];
      this.setChild(new List(), 0);
   }

   public BoundMethodAccess(String p0, List<Expr> p1) {
      this.decl_computed = false;
      this.setID(p0);
      this.setChild(p1, 0);
   }

   public BoundMethodAccess(Symbol p0, List<Expr> p1) {
      this.decl_computed = false;
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

   public MethodDecl decl() {
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

   private MethodDecl decl_compute() {
      return this.methodDecl;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
