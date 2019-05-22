package ppg.cmds;

import ppg.atoms.Production;
import ppg.util.CodeWriter;

public class ExtendCmd implements Command {
   private Production prod;

   public ExtendCmd(Production p) {
      this.prod = p;
   }

   public Production getProduction() {
      return this.prod;
   }

   public void unparse(CodeWriter cw) {
      cw.write("ExtendCmd");
      cw.allowBreak(2);
      this.prod.unparse(cw);
   }
}
