package polyglot.lex;

import java_cup.runtime.Symbol;
import polyglot.util.Position;

public abstract class Token {
   Position position;
   int symbol;

   public Token(Position position, int symbol) {
      this.position = position;
      this.symbol = symbol;
   }

   public Position getPosition() {
      return this.position;
   }

   public Symbol symbol() {
      return new Symbol(this.symbol, this);
   }

   protected static String escape(String s) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < s.length(); ++i) {
         switch(s.charAt(i)) {
         case '\t':
            sb.append("\\t");
            break;
         case '\n':
            sb.append("\\n");
            break;
         case '\u000b':
         default:
            if (s.charAt(i) >= ' ' && (s.charAt(i) <= '~' || s.charAt(i) >= 255)) {
               sb.append(s.charAt(i));
            } else {
               sb.append("\\" + Integer.toOctalString(s.charAt(i)));
            }
            break;
         case '\f':
            sb.append("\\f");
         }
      }

      return sb.toString();
   }
}
