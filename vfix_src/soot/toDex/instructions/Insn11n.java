package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn11n extends AbstractInsn implements OneRegInsn {
   private byte litB;

   public Insn11n(Opcode opc, Register regA, byte litB) {
      super(opc);
      this.regs.add(regA);
      this.litB = litB;
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public byte getLitB() {
      return this.litB;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction11n(this.opc, (byte)this.getRegA().getNumber(), this.getLitB());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsByte()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }

   public String toString() {
      return super.toString() + " lit: " + this.getLitB();
   }
}
