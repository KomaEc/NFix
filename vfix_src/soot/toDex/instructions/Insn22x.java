package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn22x extends AbstractInsn implements TwoRegInsn {
   public Insn22x(Opcode opc, Register regA, Register regB) {
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
      return new BuilderInstruction22x(this.opc, (short)this.getRegA().getNumber(), this.getRegB().getNumber());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(2);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      if (!this.getRegB().fitsUnconstrained()) {
         incompatRegs.set(1);
      }

      return incompatRegs;
   }
}
