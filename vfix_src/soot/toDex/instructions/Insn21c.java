package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn21c extends AbstractInsn implements OneRegInsn {
   private Reference referencedItem;

   public Insn21c(Opcode opc, Register regA, Reference referencedItem) {
      super(opc);
      this.regs.add(regA);
      this.referencedItem = referencedItem;
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction21c(this.opc, (short)this.getRegA().getNumber(), this.referencedItem);
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }

   public String toString() {
      return super.toString() + " ref: " + this.referencedItem;
   }
}
