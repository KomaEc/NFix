package ppg.atoms;

import java.util.Vector;

public class TerminalList {
   private String type;
   private Vector symbols;

   public TerminalList(String type, Vector syms) {
      this.type = type;
      this.symbols = syms;
   }

   public String toString() {
      String result = "TERMINAL ";
      if (this.type != null) {
         result = result + this.type;
      }

      for(int i = 0; i < this.symbols.size(); ++i) {
         result = result + (String)this.symbols.elementAt(i);
         if (i < this.symbols.size() - 1) {
            result = result + ", ";
         }
      }

      return result + ";";
   }
}
