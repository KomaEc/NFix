package ppg.atoms;

import ppg.util.CodeWriter;
import ppg.util.Equatable;

public class PrecedenceModifier extends GrammarPart implements Equatable {
   protected String terminalName;

   public String getTerminalName() {
      return this.terminalName;
   }

   public PrecedenceModifier(String terminalName) {
      this.terminalName = terminalName;
   }

   public Object clone() {
      return new PrecedenceModifier(this.getTerminalName());
   }

   public void unparse(CodeWriter cw) {
      cw.begin(0);
      cw.write("%prec ");
      cw.write(this.getTerminalName());
      cw.end();
   }

   public String toString() {
      return "%prec " + this.getTerminalName();
   }
}
