package ppg.atoms;

import java.util.Vector;

public class Precedence {
   public static final int LEFT = 0;
   public static final int RIGHT = 1;
   public static final int NONASSOC = 2;
   private int type;
   private Vector symbols;

   public Precedence(int type, Vector syms) {
      this.type = type;
      this.symbols = syms;
   }

   public Object clone() {
      Vector newSyms = new Vector();

      for(int i = 0; i < this.symbols.size(); ++i) {
         newSyms.addElement(((GrammarSymbol)this.symbols.elementAt(i)).clone());
      }

      return new Precedence(this.type, newSyms);
   }

   public String toString() {
      String result = "precedence ";
      switch(this.type) {
      case 0:
         result = result + "left ";
         break;
      case 1:
         result = result + "right ";
         break;
      case 2:
         result = result + "nonassoc ";
      }

      for(int i = 0; i < this.symbols.size(); ++i) {
         result = result + this.symbols.elementAt(i);
         if (i < this.symbols.size() - 1) {
            result = result + ", ";
         }
      }

      return result + ";";
   }
}
