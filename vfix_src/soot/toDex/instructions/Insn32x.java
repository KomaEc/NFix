package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction32x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn32x extends AbstractInsn implements TwoRegInsn {
   public Insn32x(Opcode opc, Register regA, Register regB) {
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
      return new BuilderInstruction32x(this.opc, this.getRegA().getNumber(), this.getRegB().getNumber());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(2);
      if (!this.getRegA().fitsUnconstrained()) {
         incompatRegs.set(0);
      }

      if (!this.getRegB().fitsUnconstrained()) {
         incompatRegs.set(1);
      }

      return incompatRegs;
   }
}
