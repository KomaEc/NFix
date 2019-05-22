package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;

public class Insn31t extends InsnWithOffset implements OneRegInsn {
   public AbstractPayload payload = null;

   public Insn31t(Opcode opc, Register regA) {
      super(opc);
      this.regs.add(regA);
   }

   public Register getRegA() {
      return (Register)this.regs.get(0);
   }

   public void setPayload(AbstractPayload payload) {
      this.payload = payload;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderInstruction31t(this.opc, (short)this.getRegA().getNumber(), assigner.getOrCreateLabel(this.payload));
   }

   public BitSet getIncompatibleRegs() {
      BitSet incompatRegs = new BitSet(1);
      if (!this.getRegA().fitsShort()) {
         incompatRegs.set(0);
      }

      return incompatRegs;
   }

   public int getMaxJumpOffset() {
      return 32767;
   }
}
