package soot.JastAddJ;

import beaver.Symbol;

public abstract class Literal extends PrimaryExpr implements Cloneable {
   protected String tokenString_LITERAL;
   public int LITERALstart;
   public int LITERALend;
   protected boolean constant_computed = false;
   protected Constant constant_value;

   public void flushCache() {
      super.flushCache();
      this.constant_computed = false;
      this.constant_value = null;
   }

   public void flushCollectionCache() {
      super.flushCollectionCache();
   }

   public Literal clone() throws CloneNotSupportedException {
      Literal node = (Literal)super.clone();
      node.constant_computed = false;
      node.constant_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public static Literal buildBooleanLiteral(boolean value) {
      return new BooleanLiteral(value ? "true" : "false");
   }

   public static Literal buildStringLiteral(String value) {
      return new StringLiteral(value);
   }

   public void toString(StringBuffer s) {
      s.append(this.getLITERAL());
   }

   protected static String escape(String s) {
      StringBuffer result = new StringBuffer();

      for(int i = 0; i < s.length(); ++i) {
         switch(s.charAt(i)) {
         case '\b':
            result.append("\\b");
            break;
         case '\t':
            result.append("\\t");
            break;
         case '\n':
            result.append("\\n");
            break;
         case '\f':
            result.append("\\f");
            break;
         case '\r':
            result.append("\\r");
            break;
         case '"':
            result.append("\\\"");
            break;
         case '\'':
            result.append("\\'");
            break;
         case '\\':
            result.append("\\\\");
            break;
         default:
            int value = s.charAt(i);
            if (value >= ' ' && value <= '~') {
               result.append(s.charAt(i));
            } else {
               result.append(asEscape(value));
            }
         }
      }

      return result.toString();
   }

   protected static String asEscape(int value) {
      StringBuffer s = new StringBuffer("\\u");
      String hex = Integer.toHexString(value);

      for(int i = 0; i < 4 - hex.length(); ++i) {
         s.append("0");
      }

      s.append(hex);
      return s.toString();
   }

   public Literal() {
   }

   public void init$Children() {
   }

   public Literal(String p0) {
      this.setLITERAL(p0);
   }

   public Literal(Symbol p0) {
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

   public static Literal buildDoubleLiteral(double value) {
      String digits = Double.toString(value);
      NumericLiteral lit = new DoubleLiteral(digits);
      lit.setDigits(digits);
      lit.setKind(0);
      return lit;
   }

   public static Literal buildFloatLiteral(float value) {
      String digits = Float.toString(value);
      NumericLiteral lit = new FloatingPointLiteral(digits);
      lit.setDigits(digits);
      lit.setKind(0);
      return lit;
   }

   public static Literal buildIntegerLiteral(int value) {
      String digits = Integer.toHexString(value);
      NumericLiteral lit = new IntegerLiteral("0x" + digits);
      lit.setDigits(digits.toLowerCase());
      lit.setKind(1);
      return lit;
   }

   public static Literal buildLongLiteral(long value) {
      String digits = Long.toHexString(value);
      NumericLiteral lit = new LongLiteral("0x" + digits);
      lit.setDigits(digits.toLowerCase());
      lit.setKind(1);
      return lit;
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
      throw new UnsupportedOperationException("ConstantExpression operation constant not supported for type " + this.getClass().getName());
   }

   public boolean isConstant() {
      ASTNode$State state = this.state();
      return true;
   }

   public String dumpString() {
      ASTNode$State state = this.state();
      return this.getClass().getName() + " [" + this.getLITERAL() + "]";
   }

   public ASTNode rewriteTo() {
      return super.rewriteTo();
   }
}
