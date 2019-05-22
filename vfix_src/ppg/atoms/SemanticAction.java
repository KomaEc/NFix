package ppg.atoms;

import ppg.util.CodeWriter;

public class SemanticAction extends GrammarPart {
   private String action;

   public SemanticAction(String actionCode) {
      this.action = actionCode;
   }

   public Object clone() {
      return new SemanticAction(this.action.toString());
   }

   public void unparse(CodeWriter cw) {
      cw.begin(0);
      cw.write("{:");
      cw.allowBreak(-1);
      cw.write(this.action);
      cw.allowBreak(0);
      cw.write(":}");
      cw.end();
   }

   public String toString() {
      return "{:" + this.action + ":}\n";
   }
}
