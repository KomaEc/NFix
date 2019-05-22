package soot.JastAddJ;

import beaver.Symbol;

public class ParameterDeclarationSubstituted extends ParameterDeclaration implements Cloneable {
   protected ParameterDeclaration tokenParameterDeclaration_Original;
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

   public ParameterDeclarationSubstituted clone() throws CloneNotSupportedException {
      ParameterDeclarationSubstituted node = (ParameterDeclarationSubstituted)super.clone();
      node.sourceVariableDecl_computed = false;
      node.sourceVariableDecl_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public ParameterDeclarationSubstituted copy() {
      try {
         ParameterDeclarationSubstituted node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public ParameterDeclarationSubstituted fullCopy() {
      ParameterDeclarationSubstituted tree = this.copy();
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

   public ParameterDeclarationSubstituted() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public ParameterDeclarationSubstituted(Modifiers p0, Access p1, String p2, ParameterDeclaration p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setOriginal(p3);
   }

   public ParameterDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, ParameterDeclaration p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setOriginal(p3);
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

   public void setOriginal(ParameterDeclaration value) {
      this.tokenParameterDeclaration_Original = value;
   }

   public ParameterDeclaration getOriginal() {
      return this.tokenParameterDeclaration_Original;
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

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
