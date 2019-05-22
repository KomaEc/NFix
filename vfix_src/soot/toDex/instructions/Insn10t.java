package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10t;
import soot.toDex.LabelAssigner;

public class Insn10t extends InsnWithOffset {
   public Insn10t(Opcode opc) {
      super(opc);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      if (this.target == null) {
         throw new RuntimeException("Cannot jump to a NULL target");
      } else {
         return new BuilderInstruction10t(this.opc, assigner.getOrCreateLabel(this.target));
      }
   }

   public int getMaxJumpOffset() {
      return 127;
   }
}
