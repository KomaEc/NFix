package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import soot.toDex.LabelAssigner;

public class Insn10x extends AbstractInsn {
   public Insn10x(Opcode opc) {
      super(opc);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction10x(this.opc);
   }
}
