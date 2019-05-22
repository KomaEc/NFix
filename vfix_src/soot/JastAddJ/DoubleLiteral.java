package soot.JastAddJ;

import beaver.Symbol;
import soot.Value;
import soot.jimple.DoubleConstant;

public class DoubleLiteral extends NumericLiteral implements Cloneable {
   protected boolean type_computed = false;
   protected TypeDecl type_value;
   protected boolean isZero_computed = false;
   protected boolean isZero_value;
   protected boolean constant_computed = false;
   protected Constant constant_value;

   public void flushCache() {
      super.flushCache();
      this.type_computed = false;
      this.type_value = null;
      this.isZero_computed = false;
      this.constant_computed = false;
      this.constant_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public DoubleLiteral clone() throws CloneNotSupportedException {
      DoubleLiteral node = (DoubleLiteral)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.isZero_computed = false;
      node.constant_computed = false;
      node.constant_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public DoubleLiteral copy() {
      try {
         DoubleLiteral node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public DoubleLiteral fullCopy() {
      DoubleLiteral tree = this.copy();
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

   public void typeCheck() {
      if (!this.isZero() && this.constant().doubleValue() == 0.0D) {
         this.error("It is an error for nonzero floating-point " + this.getLITERAL() + " to round to zero");
      }

      if (this.constant().doubleValue() == Double.NEGATIVE_INFINITY || this.constant().doubleValue() == Double.POSITIVE_INFINITY) {
         this.error("It is an error for floating-point " + this.getLITERAL() + " to round to an infinity");
      }

   }

   public Value eval(Body b) {
      return DoubleConstant.v(this.constant().doubleValue());
   }

   public DoubleLiteral() {
   }

   public void init$Children() {
   }

   public DoubleLiteral(String p0) {
      this.setLITERAL(p0);
   }

   public DoubleLiteral(Symbol p0) {
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

   public void toString(StringBuffer s) {
      super.toString(s);
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
      return this.typeDouble();
   }

   public boolean isZero() {
      if (this.isZero_computed) {
         return this.isZero_value;
      } else {
         ASTNode$State state = this.state();
         int num = state.boundariesCrossed;
         boolean isFinal = this.is$Final();
         this.isZero_value = this.isZero_compute();
         if (isFinal && num == this.state().boundariesCrossed) {
            this.isZero_computed = true;
         }

         return this.isZero_value;
      }
   }

   private boolean isZero_compute() {
      for(int i = 0; i < this.digits.length(); ++i) {
         char c = this.digits.charAt(i);
         if (c == 'e' || c == 'p') {
            break;
         }

         if (c != '0' && c != '.') {
            return false;
         }
      }

      return true;
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
      try {
         return Constant.create(Double.parseDouble(this.getDigits()));
      } catch (NumberFormatException var3) {
         Constant c = Constant.create(0.0D);
         c.error = true;
         return c;
      }
   }

   public boolean needsRewrite() {
      ASTNode$State state = this.state();
      return false;
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
