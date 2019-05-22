package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21s;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn21s extends AbstractInsn implements OneRegInsn {
   private short litB;

   public Insn21s(Opcode opc, Register regA, short litB) {
      super(opc);
      this.regs.add(regA);
      this.litB = litB;
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public short getLitB() {
      return this.litB;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction21s(this.opc, (short)this.getRegA().getNumber(), this.getLitB());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }

   public String toString() {
      return super.toString() + " lit: " + this.getLitB();
   }
}
