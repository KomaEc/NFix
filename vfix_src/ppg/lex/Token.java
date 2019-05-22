package ppg.lex;

import java.io.IOException;
import java.io.OutputStream;
import java_cup.runtime.Symbol;

public class Token implements LexerResult {
   private Symbol symbol;
   private String filename;
   private int lineno;
   private Object value;
   static int lastID;

   public Token(String filename, int lineno, Object value) {
      this(-1, filename, lineno, -1, -1, value);
   }

   public Token(int id, String filename, int lineno, int left, int right, Object value) {
      this.symbol = new Symbol(id, left, right, this);
      lastID = id;
      this.filename = filename;
      this.lineno = lineno;
      this.value = value;
   }

   public int getCode() {
      return this.symbol.sym;
   }

   public Symbol getSymbol() {
      return this.symbol;
   }

   public Object getValue() {
      return this.value;
   }

   public String getID() {
      return toString(this.symbol.sym);
   }

   public static String toString(int type) {
      switch(type) {
      case 0:
         return "EOF";
      case 1:
         return "ERROR";
      case 2:
         return "INCLUDE";
      case 3:
         return "TO";
      case 4:
         return "DROP";
      case 5:
         return "TRANSFER";
      case 6:
         return "LBRACE";
      case 7:
         return "RBRACE";
      case 8:
      case 9:
      default:
         System.out.println("Invalid token conversion: " + type);
         System.exit(2);
         return null;
      case 10:
         return "COMMA";
      case 11:
         return "SEMI";
      case 12:
         return "COLON";
      case 13:
         return "CCEQ";
      case 14:
         return "BAR";
      case 15:
         return "TERMINAL";
      case 16:
         return "NONTERMINAL";
      case 17:
         return "PRECEDENCE";
      case 18:
         return "DOT";
      case 19:
         return "LBRACK";
      case 20:
         return "RBRACK";
      case 21:
         return "PACKAGE";
      case 22:
         return "IMPORT";
      case 23:
         return "CODE";
      case 24:
         return "ACTION";
      case 25:
         return "PARSER";
      case 26:
         return "NON";
      case 27:
         return "INIT";
      case 28:
         return "SCAN";
      case 29:
         return "WITH";
      case 30:
         return "START";
      case 31:
         return "STAR";
      case 32:
         return "PRECEDENCE";
      case 33:
         return "LEFT";
      case 34:
         return "RIGHT";
      case 35:
         return "NONASSOC";
      case 36:
         return "STRING_CONST";
      case 37:
         return "ID";
      case 38:
         return "CODE_STR";
      case 39:
         return "EXTEND";
      case 40:
         return "OVERRIDE";
      }
   }

   public String toString() {
      return (String)this.value;
   }

   public void unparse(OutputStream o) {
      if (this.value != null) {
         try {
            o.write((this.filename + ":" + this.lineno + ": " + this.getID() + ": \"" + this.value + "\"").getBytes());
         } catch (IOException var3) {
         }
      }

   }

   public String getFilename() {
      return this.filename;
   }

   public int lineNumber() {
      return this.lineno;
   }

   public int getLineno() {
      return this.lineno;
   }

   public void setLineno(int i) {
      this.lineno = i;
   }
}
