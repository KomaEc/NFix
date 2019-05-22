package soot.JastAddJ;

import beaver.Symbol;
import soot.Value;

public class IntegerLiteral extends NumericLiteral implements Cloneable {
   protected boolean type_computed;
   protected TypeDecl type_value;
   protected boolean constant_computed;
   protected Constant constant_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
      this.constant_computed = false;
      this.constant_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public IntegerLiteral clone() throws CloneNotSupportedException {
      IntegerLiteral node = (IntegerLiteral)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.constant_computed = false;
      node.constant_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public IntegerLiteral copy() {
      try {
         IntegerLiteral node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public IntegerLiteral fullCopy() {
      IntegerLiteral tree = this.copy();
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

   public IntegerLiteral(int i) {
      this(Integer.toString(i));
   }

   public Value eval(Body b) {
      return IntType.emitConstant(this.constant().intValue());
   }

   public IntegerLiteral() {
      this.type_computed = false;
      this.constant_computed = false;
   }

   public void init$Children() {
   }

   public IntegerLiteral(String p0) {
      this.type_computed = false;
      this.constant_computed = false;
      this.setLITERAL(p0);
   }

   public IntegerLiteral(Symbol p0) {
      this.type_computed = false;
      this.constant_computed = false;
      this.setLITERAL(p0);
   }

   protected int numChildren() {
      return 0;
   }

   public boolean mayHaveRewrite() {
      return true;
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

   public void typeCheck() {
      if (this.constant().error) {
         this.error("The integer literal \"" + this.getLITERAL() + "\" is too large for type int.");
      }

   }

   public boolean isPositive() {
      ASTNode$State state = this.state();
      return !this.getLITERAL().startsWith("-");
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
      return this.typeInt();
   }

   public Constant constant() {
      if (this.constant_computed) {
         return this.constant_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.constant_value = this.constant_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.constant_computed = true;
         }

         return this.constant_value;
      }
   }

   private Constant constant_compute() {
      long l = 0L;

      try {
         l = this.parseLong();
      } catch (NumberFormatException var5) {
         Constant c = Constant.create(0L);
         c.error = true;
         return c;
      }

      Constant c = Constant.create((int)l);
      if (l != (4294967295L & (long)((int)l)) && l != (long)((int)l)) {
         c.error = true;
      }

      return c;
   }

   public boolean needsRewrite() {
      ASTNode$State state = this.state();
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
