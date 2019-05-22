package groovyjarjarasm.asm.commons;

import java.util.BitSet;

public class JSRInlinerAdapter$Subroutine {
   public final BitSet instructions = new BitSet();

   protected JSRInlinerAdapter$Subroutine() {
   }

   public void addInstruction(int var1) {
      this.instructions.set(var1);
   }

   public boolean ownsInstruction(int var1) {
      return this.instructions.get(var1);
   }

   public String toString() {
      return "Subroutine: " + this.instructions;
   }
}
