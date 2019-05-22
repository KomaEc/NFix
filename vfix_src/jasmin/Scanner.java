package jasmin;

import jas.jasError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java_cup.runtime.Symbol;

class Scanner implements java_cup.runtime.Scanner {
   InputStreamReader inp;
   int next_char;
   char[] chars;
   char[] secondChars;
   char[] unicodeBuffer;
   boolean is_first_sep;
   static final String WHITESPACE = " \n\t\r";
   static final String SEPARATORS = " \n\t\r:=";
   public int line_num;
   public int char_num;
   public int token_line_num;
   public StringBuffer line;
   public Hashtable<String, Object> dict = new Hashtable();
   static final int BIGNUM = 65000;

   protected static boolean whitespace(int c) {
      return " \n\t\r".indexOf(c) != -1;
   }

   protected static boolean separator(int c) {
      return " \n\t\r:=".indexOf(c) != -1;
   }

   protected void advance() throws IOException {
      this.next_char = this.inp.read();
      if (this.next_char == 10) {
         ++this.line_num;
         this.char_num = 0;
         this.line.setLength(0);
      } else {
         this.line.append((char)this.next_char);
         ++this.char_num;
      }

   }

   public Scanner(InputStream i) throws IOException {
      this.inp = new InputStreamReader(i);
      this.line_num = 1;
      this.char_num = 0;
      this.line = new StringBuffer();
      this.chars = new char['\ufde8'];
      this.secondChars = new char['\ufde8'];
      this.unicodeBuffer = new char[4];
      this.is_first_sep = true;
      this.advance();
   }

   int readOctal(int firstChar) throws IOException {
      this.advance();
      int d2 = this.next_char;
      this.advance();
      int d3 = this.next_char;
      return (firstChar - 48 & 7) * 64 + (d2 - 48 & 7) * 8 + (d3 - 48 & 7);
   }

   public Symbol next_token() throws IOException, jasError {
      this.token_line_num = this.line_num;

      while(true) {
         int pos;
         char[] tmparray;
         switch(this.next_char) {
         case -1:
            this.is_first_sep = false;
            this.char_num = -1;
            this.line.setLength(0);
            return new Symbol(0);
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 11:
         case 12:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 60:
         default:
            int pos = 0;
            this.chars[0] = (char)this.next_char;
            this.is_first_sep = false;
            pos = pos + 1;

            while(true) {
               this.advance();
               if (separator(this.next_char)) {
                  int secondPos = this.translateUnicodeCharacters(pos);
                  String str = new String(this.secondChars, 0, secondPos);
                  Symbol tok;
                  if ((tok = this.tryParseAsNumber(str)) != null) {
                     return tok;
                  }

                  if ((tok = ReservedWords.get(str)) == null && (tok = ReservedInstructions.get(str)) == null) {
                     return new Symbol(285, str);
                  }

                  return tok;
               }

               try {
                  this.chars[pos] = (char)this.next_char;
               } catch (ArrayIndexOutOfBoundsException var6) {
                  tmparray = new char[this.chars.length * 2];
                  System.arraycopy(this.chars, 0, tmparray, 0, this.chars.length);
                  this.chars = tmparray;
                  this.chars[pos] = (char)this.next_char;
               }

               ++pos;
            }
         case 9:
         case 13:
         case 32:
            this.advance();
            break;
         case 34:
            pos = 0;
            this.is_first_sep = false;

            while(true) {
               this.advance();
               if (this.next_char == 92) {
                  this.advance();
                  switch(this.next_char) {
                  case 34:
                     this.next_char = 34;
                     break;
                  case 39:
                     this.next_char = 39;
                     break;
                  case 48:
                  case 49:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 55:
                     this.next_char = this.readOctal(this.next_char);
                     break;
                  case 92:
                     this.next_char = 92;
                     break;
                  case 98:
                     this.next_char = 8;
                     break;
                  case 102:
                     this.next_char = 12;
                     break;
                  case 110:
                     this.next_char = 10;
                     break;
                  case 114:
                     this.next_char = 13;
                     break;
                  case 116:
                     this.next_char = 9;
                     break;
                  case 117:
                     this.advance();
                     this.unicodeBuffer[0] = (char)this.next_char;
                     this.advance();
                     this.unicodeBuffer[1] = (char)this.next_char;
                     this.advance();
                     this.unicodeBuffer[2] = (char)this.next_char;
                     this.advance();
                     this.unicodeBuffer[3] = (char)this.next_char;
                     this.next_char = (char)Integer.parseInt(new String(this.unicodeBuffer, 0, 4), 16);
                     break;
                  default:
                     throw new jasError("Bad backslash escape sequence");
                  }
               } else if (this.next_char == 34 || this.next_char < 0) {
                  this.advance();
                  return new Symbol(284, new String(this.chars, 0, pos));
               }

               try {
                  if (this.chars.length == pos) {
                     char[] newChars = new char[this.chars.length * 2];
                     System.arraycopy(this.chars, 0, newChars, 0, this.chars.length);
                     this.chars = newChars;
                  }

                  this.chars[pos] = (char)this.next_char;
               } catch (ArrayIndexOutOfBoundsException var5) {
                  tmparray = new char[this.chars.length * 2];
                  System.arraycopy(this.chars, 0, tmparray, 0, this.chars.length);
                  this.chars = tmparray;
                  this.chars[pos] = (char)this.next_char;
               }

               ++pos;
            }
         case 58:
            this.advance();
            this.is_first_sep = false;
            return new Symbol(75);
         case 59:
            do {
               this.advance();
            } while(this.next_char != 10);
         case 10:
            while(true) {
               do {
                  this.advance();
               } while(whitespace(this.next_char));

               if (this.next_char != 59) {
                  if (this.is_first_sep) {
                     return this.next_token();
                  }

                  this.token_line_num = this.line_num;
                  return new Symbol(74);
               }

               while(true) {
                  this.advance();
                  if (this.next_char == 10) {
                     break;
                  }
               }
            }
         case 61:
            this.advance();
            this.is_first_sep = false;
            return new Symbol(73);
         }
      }
   }

   private Symbol tryParseAsNumber(String str) throws jasError {
      if (str.isEmpty()) {
         return null;
      } else if (str.equals("+DoubleInfinity")) {
         return new Symbol(287, new Double(Double.POSITIVE_INFINITY));
      } else if (str.equals("+DoubleNaN")) {
         return new Symbol(287, new Double(Double.NaN));
      } else if (str.equals("+FloatNaN")) {
         return new Symbol(287, new Float(Double.NaN));
      } else if (str.equals("-DoubleInfinity")) {
         return new Symbol(287, new Double(Double.NEGATIVE_INFINITY));
      } else if (str.equals("+FloatInfinity")) {
         return new Symbol(287, new Float(Float.POSITIVE_INFINITY));
      } else if (str.equals("-FloatInfinity")) {
         return new Symbol(287, new Float(Float.NEGATIVE_INFINITY));
      } else {
         Symbol tok;
         if ((tok = ReservedWords.get(str)) != null) {
            return tok;
         } else {
            int idxOpen = str.indexOf("(");
            int idxClose = str.indexOf(")");
            if (idxOpen > 0 && idxClose > idxOpen) {
               return null;
            } else {
               char c = str.charAt(0);
               if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9' && c != '-' && c != '+' && c != '.') {
                  return null;
               } else {
                  Number num;
                  try {
                     num = ScannerUtils.convertNumber(str);
                  } catch (NumberFormatException var8) {
                     if (this.chars[0] == '.') {
                        throw new jasError("Unknown directive or badly formed number.");
                     }

                     throw new jasError("Badly formatted number: " + str);
                  }

                  return num instanceof Integer ? new Symbol(286, new Integer(num.intValue())) : new Symbol(287, num);
               }
            }
         }
      }
   }

   private int translateUnicodeCharacters(int pos) {
      int secondPos = 0;

      for(int i = 0; i < pos; ++i) {
         if (this.chars[i] == '\\' && i + 5 < pos && this.chars[i + 1] == 'u') {
            int intValue = Integer.parseInt(new String(this.chars, i + 2, 4), 16);

            try {
               this.secondChars[secondPos] = (char)intValue;
            } catch (ArrayIndexOutOfBoundsException var7) {
               char[] tmparray = new char[this.secondChars.length * 2];
               System.arraycopy(this.secondChars, 0, tmparray, 0, this.secondChars.length);
               this.secondChars = tmparray;
               this.secondChars[secondPos] = (char)intValue;
            }

            ++secondPos;
            i += 5;
         } else {
            try {
               this.secondChars[secondPos] = this.chars[i];
            } catch (ArrayIndexOutOfBoundsException var8) {
               char[] tmparray = new char[this.secondChars.length * 2];
               System.arraycopy(this.secondChars, 0, tmparray, 0, this.secondChars.length);
               this.secondChars = tmparray;
               this.secondChars[secondPos] = this.chars[i];
            }

            ++secondPos;
         }
      }

      return secondPos;
   }
}
