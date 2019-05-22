package ppg.cmds;

import ppg.atoms.Production;
import ppg.util.CodeWriter;

public class NewProdCmd implements Command {
   private Production prod;

   public NewProdCmd(Production p) {
      this.prod = p;
   }

   public Production getProduction() {
      return this.prod;
   }

   public void unparse(CodeWriter cw) {
      cw.write("NewProdCmd");
      cw.allowBreak(0);
      this.prod.unparse(cw);
   }
}
