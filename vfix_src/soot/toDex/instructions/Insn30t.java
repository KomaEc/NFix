package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction30t;
import soot.toDex.LabelAssigner;

public class Insn30t extends InsnWithOffset {
   public Insn30t(Opcode opc) {
      super(opc);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction30t(this.opc, assigner.getOrCreateLabel(this.target));
   }

   public int getMaxJumpOffset() {
      return Integer.MAX_VALUE;
   }
}
