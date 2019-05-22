package ppg.atoms;

import ppg.util.CodeWriter;
import ppg.util.Equatable;

public abstract class GrammarSymbol extends GrammarPart implements Equatable {
   protected String name;
   protected String label;

   public String getName() {
      return this.name;
   }

   public void unparse(CodeWriter cw) {
      cw.begin(0);
      cw.write(this.name);
      if (this.label != null) {
         cw.write(":" + this.label);
      }

      cw.end();
   }

   public String toString() {
      String result = this.name;
      if (this.label != null) {
         result = result + ":" + this.label;
      }

      return result;
   }
}
