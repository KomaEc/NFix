package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn22t extends InsnWithOffset implements TwoRegInsn {
   public Insn22t(Opcode opc, Register regA, Register regB) {
      super(opc);
      this.regs.add(regA);
      this.regs.add(regB);
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public Register getRegB() {
      return (Register)this.regs.get(1);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction22t(this.opc, (byte)this.getRegA().getNumber(), (byte)this.getRegB().getNumber(), assigner.getOrCreateLabel(this.target));
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(2);
      if (!this.getRegA().fitsByte()) {
         incompatRegs.set(0);
      }

      if (!this.getRegB().fitsByte()) {
         incompatRegs.set(1);
      }

      return incompatRegs;
   }

   public int getMaxJumpOffset() {
      return 32767;
   }
}
