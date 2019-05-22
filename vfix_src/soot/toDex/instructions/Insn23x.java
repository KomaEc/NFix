package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn23x extends AbstractInsn implements ThreeRegInsn {
   public Insn23x(Opcode opc, Register regA, Register regB, Register regC) {
      super(opc);
      this.regs.add(regA);
      this.regs.add(regB);
      this.regs.add(regC);
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public Register getRegB() {
      return (Register)this.regs.get(1);
   }

   public Register getRegC() {
      return (Register)this.regs.get(2);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction23x(this.opc, (short)this.getRegA().getNumber(), (short)this.getRegB().getNumber(), (short)this.getRegC().getNumber());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(3);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      if (!this.getRegB().fitsShort()) {
         incompatRegs.set(1);
      }

      if (!this.getRegC().fitsShort()) {
         incompatRegs.set(2);
      }

      return incompatRegs;
   }
}
