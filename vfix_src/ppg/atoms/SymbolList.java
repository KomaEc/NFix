package ppg.atoms;

import java.util.Vector;

public class SymbolList {
   public static final int TERMINAL = 0;
   public static final int NONTERMINAL = 1;
   private int variety;
   private String type;
   private Vector symbols;

   public SymbolList(int which, String type, Vector syms) {
      this.variety = which;
      this.type = type;
      this.symbols = syms;
   }

   public boolean dropSymbol(String gs) {
      for(int i = 0; i < this.symbols.size(); ++i) {
         if (gs.equals(this.symbols.elementAt(i))) {
            this.symbols.removeElementAt(i);
            return true;
         }
      }

      return false;
   }

   public Object clone() {
      String newType = this.type == null ? null : this.type.toString();
      Vector newSyms = new Vector();

      for(int i = 0; i < this.symbols.size(); ++i) {
         newSyms.addElement(((String)this.symbols.elementAt(i)).toString());
      }

      return new SymbolList(this.variety, newType, newSyms);
   }

   public String toString() {
      String result = "";
      if (this.symbols.size() > 0) {
         switch(this.variety) {
         case 0:
            result = "terminal ";
            break;
         case 1:
            result = "non terminal ";
         }

         if (this.type != null) {
            result = result + this.type + " ";
         }

         int size = this.symbols.size();

         for(int i = 0; i < size; ++i) {
            result = result + (String)this.symbols.elementAt(i);
            if (i < size - 1) {
               result = result + ", ";
            }
         }

         result = result + ";";
      }

      return result;
   }
}
