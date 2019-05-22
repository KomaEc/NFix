package soot.JastAddJ;

import beaver.Symbol;
import soot.Value;
import soot.jimple.NullConstant;

public class NullLiteral extends Literal implements Cloneable {
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

   public NullLiteral clone() throws CloneNotSupportedException {
      NullLiteral node = (NullLiteral)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public NullLiteral copy() {
      try {
         NullLiteral node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public NullLiteral fullCopy() {
      NullLiteral tree = this.copy();
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

   public Value eval(Body b) {
      return NullConstant.v();
   }

   public NullLiteral() {
   }

   public void init$Children() {
   }

   public NullLiteral(String p0) {
      this.setLITERAL(p0);
   }

   public NullLiteral(Symbol p0) {
      this.setLITERAL(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return false;
   }

   public void setLITERAL(String value) {
      this.tokenString_LITERAL = value;
   }

   public void setLITERAL(Symbol symbol) {
      if (symbol.value != null && !(symbol.value instanceof String)) {
         throw new UnsupportedOperationException("setLITERAL is only valid for String lexemes");
      } else {
         this.tokenString_LITERAL = (String)symbol.value;
         this.LITERALstart = symbol.getStart();
         this.LITERALend = symbol.getEnd();
      }
   }

   public String getLITERAL() {
      return this.tokenString_LITERAL != null ? this.tokenString_LITERAL : "";
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return false;
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
      return this.typeNull();
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
