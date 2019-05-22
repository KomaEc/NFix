package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20t;
import soot.toDex.LabelAssigner;

public class Insn20t extends InsnWithOffset {
   public Insn20t(Opcode opc) {
      super(opc);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction20t(this.opc, assigner.getOrCreateLabel(this.target));
   }

   public int getMaxJumpOffset() {
      return 32767;
   }
}
