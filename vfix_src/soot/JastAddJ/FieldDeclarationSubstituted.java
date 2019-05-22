package soot.JastAddJ;

import beaver.Symbol;

public class FieldDeclarationSubstituted extends FieldDeclaration implements Cloneable {
   protected FieldDeclaration tokenFieldDeclaration_Original;
   protected boolean sourceVariableDecl_computed = false;
   protected Variable sourceVariableDecl_value;

   public void flushCache() {
      super.flushCache();
      this.sourceVariableDecl_computed = false;
      this.sourceVariableDecl_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public FieldDeclarationSubstituted clone() throws CloneNotSupportedException {
      FieldDeclarationSubstituted node = (FieldDeclarationSubstituted)super.clone();
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public FieldDeclarationSubstituted copy() {
      try {
         FieldDeclarationSubstituted node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public FieldDeclarationSubstituted fullCopy() {
      FieldDeclarationSubstituted tree = this.copy();
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

   public FieldDeclarationSubstituted() {
   }

   public void init$Children() {
      this.children = new ASTNode[3];
      this.setChild(new Opt(), 2);
   }

   public FieldDeclarationSubstituted(Modifiers p0, Access p1, String p2, Opt<Expr> p3, FieldDeclaration p4) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setOriginal(p4);
   }

   public FieldDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, Opt<Expr> p3, FieldDeclaration p4) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setChild(p3, 2);
      this.setOriginal(p4);
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

   public void setOriginal(FieldDeclaration value) {
      this.tokenFieldDeclaration_Original = value;
   }

   public FieldDeclaration getOriginal() {
      return this.tokenFieldDeclaration_Original;
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
      return this.getOriginal().sourceVariableDecl();
   }

   public FieldDeclaration erasedField() {
      ASTNode$State state = this.state();
      return this.getOriginal().erasedField();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
