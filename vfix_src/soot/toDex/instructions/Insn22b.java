package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22b;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn22b extends AbstractInsn implements TwoRegInsn {
   private byte litC;

   public Insn22b(Opcode opc, Register regA, Register regB, byte litC) {
      super(opc);
      this.regs.add(regA);
      this.regs.add(regB);
      this.litC = litC;
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public Register getRegB() {
      return (Register)this.regs.get(1);
   }

   public byte getLitC() {
      return this.litC;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction22b(this.opc, (short)this.getRegA().getNumber(), (short)this.getRegB().getNumber(), this.getLitC());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(2);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      if (!this.getRegB().fitsShort()) {
         incompatRegs.set(1);
      }

      return incompatRegs;
   }

   public String toString() {
      return super.toString() + " lit: " + this.getLitC();
   }
}
