package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22s;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn22s extends AbstractInsn implements TwoRegInsn {
   private short litC;

   public Insn22s(Opcode opc, Register regA, Register regB, short litC) {
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

   public short getLitC() {
      return this.litC;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction22s(this.opc, (byte)this.getRegA().getNumber(), (byte)this.getRegB().getNumber(), this.getLitC());
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

   public String toString() {
      return super.toString() + " lit: " + this.getLitC();
   }
}
