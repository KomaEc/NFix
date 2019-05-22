package soot.JastAddJ;

import beaver.Symbol;

public class NumericLiteral extends Literal implements Cloneable {
   public static final int DECIMAL = 0;
   public static final int HEXADECIMAL = 1;
   public static final int OCTAL = 2;
   public static final int BINARY = 3;
   protected String digits = "";
   protected int kind = 0;
   private StringBuffer buf = new StringBuffer();
   private int idx = 0;
   private boolean whole;
   private boolean fraction;
   private boolean exponent;
   private boolean floating;
   private boolean isFloat;
   private boolean isLong;
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

   public NumericLiteral clone() throws CloneNotSupportedException {
      NumericLiteral node = (NumericLiteral)super.clone();
      node.type_computed = false;
      node.type_value = null;
      node.in$Circle(false);
      node.is$Final(false);
      return node;
   }

   public NumericLiteral copy() {
      try {
         NumericLiteral node = this.clone();
         node.parent = null;
         if (this.children != null) {
            node.children = (ASTNode[])((ASTNode[])this.children.clone());
         }

         return node;
      } catch (CloneNotSupportedException var2) {
         throw new Error("Error: clone not supported for " + this.getClass().getName());
      }
   }

   public NumericLiteral fullCopy() {
      NumericLiteral tree = this.copy();
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

   public void setDigits(String digits) {
      this.digits = digits;
   }

   public void setKind(int kind) {
      this.kind = kind;
   }

   private String name() {
      String name;
      switch(this.kind) {
      case 0:
         name = "decimal";
         break;
      case 1:
         name = "hexadecimal";
         break;
      case 2:
         name = "octal";
         break;
      case 3:
      default:
         name = "binary";
      }

      return this.floating ? name + " floating point" : name;
   }

   private void pushChar() {
      this.buf.append(this.getLITERAL().charAt(this.idx++));
   }

   private void skip(int n) {
      this.idx += n;
   }

   private boolean have(int n) {
      return this.getLITERAL().length() >= this.idx + n;
   }

   private char peek(int n) {
      return this.getLITERAL().charAt(this.idx + n);
   }

   private static final boolean isDecimalDigit(char c) {
      return c == '_' || c >= '0' && c <= '9';
   }

   private static final boolean isHexadecimalDigit(char c) {
      return c == '_' || c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
   }

   private static final boolean isBinaryDigit(char c) {
      return c == '_' || c == '0' || c == '1';
   }

   private static final boolean isUnderscore(char c) {
      return c == '_';
   }

   public Literal parse() {
      if (this.getLITERAL().length() == 0) {
         throw new IllegalStateException("Empty NumericLiteral");
      } else {
         this.kind = this.classifyLiteral();
         Literal literal;
         if (!this.floating) {
            literal = this.parseDigits();
         } else {
            literal = this.parseFractionPart();
         }

         literal.setStart(this.LITERALstart);
         literal.setEnd(this.LITERALend);
         return literal;
      }
   }

   private int classifyLiteral() {
      if (this.peek(0) == '.') {
         this.floating = true;
         return 0;
      } else if (this.peek(0) == '0') {
         if (!this.have(2)) {
            return 0;
         } else if (this.peek(1) != 'x' && this.peek(1) != 'X') {
            if (this.peek(1) != 'b' && this.peek(1) != 'B') {
               return 0;
            } else {
               this.skip(2);
               return 3;
            }
         } else {
            this.skip(2);
            return 1;
         }
      } else {
         return 0;
      }
   }

   private boolean misplacedUnderscore() {
      if (this.idx != 0 && this.idx + 1 != this.getLITERAL().length()) {
         switch(this.kind) {
         case 0:
            return !isDecimalDigit(this.peek(-1)) || !isDecimalDigit(this.peek(1));
         case 1:
            return !isHexadecimalDigit(this.peek(-1)) || !isHexadecimalDigit(this.peek(1));
         case 2:
         default:
            throw new IllegalStateException("Unexpected literal kind");
         case 3:
            return !isBinaryDigit(this.peek(-1)) || !isBinaryDigit(this.peek(1));
         }
      } else {
         return true;
      }
   }

   private Literal syntaxError(String msg) {
      String err = "in " + this.name() + " literal \"" + this.getLITERAL() + "\": " + msg;
      return new IllegalLiteral(err);
   }

   private Literal unexpectedCharacter(char c) {
      return this.syntaxError("unexpected character '" + c + "'; not a valid digit");
   }

   private String getLiteralString() {
      return this.buf.toString().toLowerCase();
   }

   private Literal buildLiteral() {
      this.setDigits(this.buf.toString().toLowerCase());
      Object literal;
      if (!this.floating) {
         if (!this.whole) {
            return this.syntaxError("at least one digit is required");
         }

         if (this.kind == 0 && this.digits.charAt(0) == '0') {
            this.kind = 2;

            for(int idx = 1; idx < this.digits.length(); ++idx) {
               char c = this.digits.charAt(idx);
               if (c < '0' || c > '7') {
                  return this.unexpectedCharacter(c);
               }
            }
         }

         if (this.isLong) {
            literal = new LongLiteral(this.getLITERAL());
         } else {
            literal = new IntegerLiteral(this.getLITERAL());
         }
      } else {
         if (this.kind == 1 && !this.exponent) {
            return this.syntaxError("exponent is required");
         }

         if (!this.whole && !this.fraction) {
            return this.syntaxError("at least one digit is required in either the whole or fraction part");
         }

         if (this.kind == 1) {
            this.digits = "0x" + this.digits;
         }

         if (this.isFloat) {
            literal = new FloatingPointLiteral(this.getLITERAL());
         } else {
            literal = new DoubleLiteral(this.getLITERAL());
         }
      }

      ((NumericLiteral)literal).setDigits(this.getDigits());
      ((NumericLiteral)literal).setKind(this.getKind());
      return (Literal)literal;
   }

   private Literal parseDigits() {
      while(this.have(1)) {
         char c = this.peek(0);
         switch(c) {
         case '.':
            if (this.kind != 0 && this.kind != 1) {
               return this.unexpectedCharacter(c);
            }

            return this.parseFractionPart();
         case 'F':
         case 'f':
            if (this.kind == 3) {
               return this.unexpectedCharacter(c);
            }

            this.isFloat = true;
         case 'D':
         case 'd':
            if (this.kind == 3) {
               return this.unexpectedCharacter(c);
            }

            if (this.kind != 1) {
               if (this.have(2)) {
                  return this.syntaxError("extra digits/characters after type suffix " + c);
               }

               this.floating = true;
               this.skip(1);
               break;
            }

            this.whole = true;
            this.pushChar();
            break;
         case 'L':
         case 'l':
            if (this.have(2)) {
               return this.syntaxError("extra digits/characters after suffix " + c);
            }

            this.isLong = true;
            this.skip(1);
            break;
         case '_':
            if (this.misplacedUnderscore()) {
               return this.syntaxError("misplaced underscore - underscores may only be used within sequences of digits");
            }

            this.skip(1);
            break;
         default:
            switch(this.kind) {
            case 0:
               if (c != 'e' && c != 'E') {
                  if (c != 'f' && c != 'F') {
                     if (c != 'd' && c != 'D') {
                        if (!isDecimalDigit(c)) {
                           return this.unexpectedCharacter(c);
                        }

                        this.whole = true;
                        this.pushChar();
                        break;
                     }

                     if (this.have(2)) {
                        return this.syntaxError("extra digits/characters after type suffix " + c);
                     }

                     this.floating = true;
                     this.skip(1);
                     break;
                  }

                  if (this.have(2)) {
                     return this.syntaxError("extra digits/characters after type suffix " + c);
                  }

                  this.floating = true;
                  this.isFloat = true;
                  this.skip(1);
                  break;
               }

               return this.parseExponentPart();
            case 1:
               if (c == 'p' || c == 'P') {
                  return this.parseExponentPart();
               }

               if (!isHexadecimalDigit(c)) {
                  return this.unexpectedCharacter(c);
               }

               this.whole = true;
               this.pushChar();
            case 2:
            default:
               break;
            case 3:
               if (!isBinaryDigit(c)) {
                  return this.unexpectedCharacter(c);
               }

               this.whole = true;
               this.pushChar();
            }
         }
      }

      return this.buildLiteral();
   }

   private Literal parseFractionPart() {
      this.floating = true;
      this.pushChar();

      while(this.have(1)) {
         char c = this.peek(0);
         switch(c) {
         case '.':
            return this.syntaxError("multiple decimal periods are not allowed");
         case '_':
            if (this.misplacedUnderscore()) {
               return this.syntaxError("misplaced underscore - underscores may only be used as separators within sequences of valid digits");
            }

            this.skip(1);
            break;
         default:
            if (this.kind == 0) {
               if (c == 'e' || c == 'E') {
                  return this.parseExponentPart();
               }

               if (c != 'f' && c != 'F') {
                  if (c != 'd' && c != 'D') {
                     if (!isDecimalDigit(c)) {
                        return this.unexpectedCharacter(c);
                     }

                     this.pushChar();
                     this.fraction = true;
                  } else {
                     if (this.have(2)) {
                        return this.syntaxError("extra digits/characters after type suffix " + c);
                     }

                     this.floating = true;
                     this.skip(1);
                  }
               } else {
                  if (this.have(2)) {
                     return this.syntaxError("extra digits/characters after type suffix " + c);
                  }

                  this.floating = true;
                  this.isFloat = true;
                  this.skip(1);
               }
            } else {
               if (c == 'p' || c == 'P') {
                  return this.parseExponentPart();
               }

               if (!isHexadecimalDigit(c)) {
                  return this.unexpectedCharacter(c);
               }

               this.fraction = true;
               this.pushChar();
            }
         }
      }

      return this.buildLiteral();
   }

   private Literal parseExponentPart() {
      this.floating = true;
      this.pushChar();
      if (this.have(1) && (this.peek(0) == '+' || this.peek(0) == '-')) {
         this.pushChar();
      }

      while(this.have(1)) {
         char c = this.peek(0);
         switch(c) {
         case '+':
         case '-':
            return this.syntaxError("exponent sign character is only allowed as the first character of the exponent part of a floating point literal");
         case '.':
            return this.syntaxError("multiple decimal periods are not allowed");
         case 'F':
         case 'f':
            this.isFloat = true;
         case 'D':
         case 'd':
            if (this.have(2)) {
               return this.syntaxError("extra digits/characters after type suffix " + c);
            }

            this.skip(1);
            break;
         case 'P':
         case 'p':
            return this.syntaxError("multiple exponent specifiers are not allowed");
         case '_':
            if (this.misplacedUnderscore()) {
               return this.syntaxError("misplaced underscore - underscores may only be used as separators within sequences of valid digits");
            }

            this.skip(1);
            break;
         default:
            if (!isDecimalDigit(c)) {
               return this.unexpectedCharacter(c);
            }

            this.pushChar();
            this.exponent = true;
         }
      }

      return this.buildLiteral();
   }

   public NumericLiteral() {
   }

   public void init$Children() {
   }

   public NumericLiteral(String p0) {
      this.setLITERAL(p0);
   }

   public NumericLiteral(Symbol p0) {
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

   public long parseLong() {
      ASTNode$State state = this.state();
      switch(this.getKind()) {
      case 0:
      default:
         return this.parseLongDecimal();
      case 1:
         return this.parseLongHexadecimal();
      case 2:
         return this.parseLongOctal();
      case 3:
         return this.parseLongBinary();
      }
   }

   public long parseLongHexadecimal() {
      ASTNode$State state = this.state();
      long val = 0L;
      int i;
      if (this.digits.length() > 16) {
         for(i = 0; i < this.digits.length() - 16; ++i) {
            if (this.digits.charAt(i) != '0') {
               throw new NumberFormatException("");
            }
         }
      }

      for(i = 0; i < this.digits.length(); ++i) {
         int c = this.digits.charAt(i);
         int c;
         if (c >= 'a' && c <= 'f') {
            c = c - 97 + 10;
         } else {
            c = c - 48;
         }

         val = val * 16L + (long)c;
      }

      return val;
   }

   public long parseLongOctal() {
      ASTNode$State state = this.state();
      long val = 0L;
      int i;
      if (this.digits.length() > 21) {
         for(i = 0; i < this.digits.length() - 21; ++i) {
            if (i == this.digits.length() - 21 - 1) {
               if (this.digits.charAt(i) != '0' && this.digits.charAt(i) != '1') {
                  throw new NumberFormatException("");
               }
            } else if (this.digits.charAt(i) != '0') {
               throw new NumberFormatException("");
            }
         }
      }

      for(i = 0; i < this.digits.length(); ++i) {
         int c = this.digits.charAt(i) - 48;
         val = val * 8L + (long)c;
      }

      return val;
   }

   public long parseLongBinary() {
      ASTNode$State state = this.state();
      long val = 0L;
      int i;
      if (this.digits.length() > 64) {
         for(i = 0; i < this.digits.length() - 64; ++i) {
            if (this.digits.charAt(i) != '0') {
               throw new NumberFormatException("");
            }
         }
      }

      for(i = 0; i < this.digits.length(); ++i) {
         if (this.digits.charAt(i) == '1') {
            val |= 1L << this.digits.length() - i - 1;
         }
      }

      return val;
   }

   public long parseLongDecimal() {
      ASTNode$State state = this.state();
      long val = 0L;
      long prev = 0L;

      for(int i = 0; i < this.digits.length(); ++i) {
         prev = val;
         int c = this.digits.charAt(i);
         if (c < '0' || c > '9') {
            throw new NumberFormatException("");
         }

         int c = c - 48;
         val = val * 10L + (long)c;
         if (val < prev) {
            boolean negMinValue = i == this.digits.length() - 1 && this.isNegative() && val == Long.MIN_VALUE;
            if (!negMinValue) {
               throw new NumberFormatException("");
            }
         }
      }

      if (val == Long.MIN_VALUE) {
         return val;
      } else if (val < 0L) {
         throw new NumberFormatException("");
      } else {
         return this.isNegative() ? -val : val;
      }
   }

   public boolean needsRewrite() {
      ASTNode$State state = this.state();
      return true;
   }

   public boolean isNegative() {
      ASTNode$State state = this.state();
      return this.getLITERAL().charAt(0) == '-';
   }

   public String getDigits() {
      ASTNode$State state = this.state();
      return this.digits;
   }

   public int getKind() {
      ASTNode$State state = this.state();
      return this.kind;
   }

   public int getRadix() {
      ASTNode$State state = this.state();
      switch(this.kind) {
      case 0:
      default:
         return 10;
      case 1:
         return 16;
      case 2:
         return 8;
      case 3:
         return 2;
      }
   }

   public boolean isDecimal() {
      ASTNode$State state = this.state();
      return this.kind == 0;
   }

   public boolean isHex() {
      ASTNode$State state = this.state();
      return this.kind == 1;
   }

   public boolean isOctal() {
      ASTNode$State state = this.state();
      return this.kind == 2;
   }

   public boolean isBinary() {
      ASTNode$State state = this.state();
      return this.kind == 3;
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
      return this.unknownType();
   }

   public ASTNode rewriteTo() {
      if (this.needsRewrite()) {
         ++this.state().duringLiterals;
         ASTNode result = this.rewriteRule0();
         --this.state().duringLiterals;
         return result;
      } else {
         return super.rewriteTo();
      }
   }

   private Literal rewriteRule0() {
      return this.parse();
   }
}
