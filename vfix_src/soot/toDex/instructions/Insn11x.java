package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn11x extends AbstractInsn implements OneRegInsn {
   public Insn11x(Opcode opc, Register regA) {
      super(opc);
      this.regs.add(regA);
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction11x(this.opc, (short)this.getRegA().getNumber());
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }
}
