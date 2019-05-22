package ppg.cmds;

import java.util.Vector;
import ppg.atoms.Nonterminal;
import ppg.atoms.Production;
import ppg.util.CodeWriter;

public class TransferCmd implements Command {
   private Nonterminal nonterminal;
   private Vector transferList;

   public TransferCmd(String nt, Vector tlist) {
      this.nonterminal = new Nonterminal(nt);
      this.transferList = tlist;
   }

   public Nonterminal getSource() {
      return this.nonterminal;
   }

   public Vector getTransferList() {
      return this.transferList;
   }

   public void unparse(CodeWriter cw) {
      cw.write("TransferCmd");
      cw.allowBreak(2);
      cw.write(this.nonterminal + " to ");

      for(int i = 0; i < this.transferList.size(); ++i) {
         Production prod = (Production)this.transferList.elementAt(i);
         prod.unparse(cw);
      }

   }
}
