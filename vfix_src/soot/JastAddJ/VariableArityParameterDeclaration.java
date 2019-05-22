package soot.JastAddJ;

import beaver.Symbol;

public class VariableArityParameterDeclaration extends ParameterDeclaration implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public VariableArityParameterDeclaration clone() throws CloneNotSupportedException {
      VariableArityParameterDeclaration node = (VariableArityParameterDeclaration)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public VariableArityParameterDeclaration copy() {
      try {
         VariableArityParameterDeclaration node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public VariableArityParameterDeclaration fullCopy() {
      VariableArityParameterDeclaration tree = this.copy();
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

   public void nameCheck() {
      super.nameCheck();
      if (!this.variableArityValid()) {
         this.error("only the last formal paramater may be of variable arity");
      }

   }

   public void toString(StringBuffer s) {
      this.getModifiers().toString(s);
      this.getTypeAccess().toString(s);
      s.append(" ... " + this.name());
   }

   public VariableArityParameterDeclaration() {
   }

   public void init$Children() {
      this.children = new ASTNode[2];
   }

   public VariableArityParameterDeclaration(Modifiers p0, Access p1, String p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
   }

   public VariableArityParameterDeclaration(Modifiers p0, Access p1, Symbol p2) {
      this.setChild(p0, 0);
      this.setChild(p1, 1);
      this.setID(p2);
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

   public TypeDecl type() {
      if (this.type_computed) {
         return this.type_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.type_value = this.type_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.type_computed = true;
         }

         return this.type_value;
      }
   }

   private TypeDecl type_compute() {
      return super.type().arrayType();
   }

   public boolean isVariableArity() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean variableArityValid() {
      ASTNode$State state = this.state();
      boolean variableArityValid_value = this.getParent().Define_boolean_variableArityValid(this, (ASTNode)null);
      return variableArityValid_value;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
