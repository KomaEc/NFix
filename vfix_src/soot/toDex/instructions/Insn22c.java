package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn22c extends AbstractInsn implements TwoRegInsn {
   private Reference referencedItem;

   public Insn22c(Opcode opc, Register regA, Register regB, Reference referencedItem) {
      super(opc);
      this.regs.add(regA);
      this.regs.add(regB);
      this.referencedItem = referencedItem;
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public Register getRegB() {
      return (Register)this.regs.get(1);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction22c(this.opc, this.getRegA().getNumber(), this.getRegB().getNumber(), this.referencedItem);
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
      return super.toString() + " ref: " + this.referencedItem;
   }
}
