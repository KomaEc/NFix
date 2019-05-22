package ppg.cmds;

import java.util.Vector;
import ppg.atoms.Production;
import ppg.util.CodeWriter;

public class DropCmd implements Command {
   private Production prod;
   private Vector sym;

   public DropCmd(Vector symbols) {
      this.sym = symbols;
      this.prod = null;
   }

   public DropCmd(Production productions) {
      this.prod = productions;
      this.sym = null;
   }

   public boolean isProdDrop() {
      return this.prod != null;
   }

   public boolean isSymbolDrop() {
      return this.sym != null;
   }

   public Production getProduction() {
      return this.prod;
   }

   public Vector getSymbols() {
      return this.sym;
   }

   public void unparse(CodeWriter cw) {
      cw.write("DropCmd");
      cw.allowBreak(0);
      if (this.prod != null) {
         this.prod.unparse(cw);
      } else {
         for(int i = 0; i < this.sym.size(); ++i) {
            cw.write((String)this.sym.elementAt(i));
         }
      }

   }
}
