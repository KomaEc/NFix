package ppg.cmds;

import ppg.atoms.Nonterminal;
import ppg.atoms.Production;
import ppg.util.CodeWriter;

public class OverrideCmd implements Command {
   private Production prod;

   public OverrideCmd(Production p) {
      this.prod = p;
   }

   public Nonterminal getLHS() {
      return this.prod.getLHS();
   }

   public Production getProduction() {
      return this.prod;
   }

   public void unparse(CodeWriter cw) {
      cw.write("OverrideCmd");
      cw.allowBreak(0);
      this.prod.unparse(cw);
   }
}
