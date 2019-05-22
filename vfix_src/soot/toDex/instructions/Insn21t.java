package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn21t extends InsnWithOffset implements OneRegInsn {
   public Insn21t(Opcode opc, Register regA) {
      super(opc);
      this.regs.add(regA);
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction21t(this.opc, (short)this.getRegA().getNumber(), assigner.getOrCreateLabel(this.target));
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }

   public int getMaxJumpOffset() {
      return 32767;
   }
}
