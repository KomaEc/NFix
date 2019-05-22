package soot.JastAddJ;

import beaver.Symbol;

public class VariableArityParameterDeclarationSubstituted extends VariableArityParameterDeclaration implements Cloneable {
   protected VariableArityParameterDeclaration tokenVariableArityParameterDeclaration_Original;

   public void flushCache() {
      super.flushCache();
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VariableArityParameterDeclarationSubstituted clone() throws CloneNotSupportedException {
      VariableArityParameterDeclarationSubstituted node = (VariableArityParameterDeclarationSubstituted)super.clone();
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VariableArityParameterDeclarationSubstituted copy() {
      try {
         VariableArityParameterDeclarationSubstituted node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VariableArityParameterDeclarationSubstituted fullCopy() {
      VariableArityParameterDeclarationSubstituted tree = this.copy();
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

   public VariableArityParameterDeclarationSubstituted() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public VariableArityParameterDeclarationSubstituted(Modifiers p0, Access p1, String p2, VariableArityParameterDeclaration p3) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
      this.setOriginal(p3);
   }

   public VariableArityParameterDeclarationSubstituted(Modifiers p0, Access p1, Symbol p2, VariableArityParameterDeclaration p3) {
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

   public void setOriginal(VariableArityParameterDeclaration value) {
      this.tokenVariableArityParameterDeclaration_Original = value;
   }

   public VariableArityParameterDeclaration getOriginal() {
      return this.tokenVariableArityParameterDeclaration_Original;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
