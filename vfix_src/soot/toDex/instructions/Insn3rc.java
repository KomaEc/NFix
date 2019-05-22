package soot.toDex.instructions;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rc;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
import soot.toDex.SootToDexUtils;

public class Insn3rc extends AbstractInsn {
   private short regCount;
   private Reference referencedItem;

   public Insn3rc(Opcode opc, List<Register> regs, short regCount, Reference referencedItem) {
      super(opc);
      this.regs = regs;
      this.regCount = regCount;
      this.referencedItem = referencedItem;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      Register startReg = (Register)this.regs.get(0);
      return new BuilderInstruction3rc(this.opc, startReg.getNumber(), this.regCount, this.referencedItem);
   }

   public BitSet getIncompatibleRegs() {
      int regCount = SootToDexUtils.getRealRegCount(this.regs);
      if (this.hasHoleInRange()) {
         return getAllIncompatible(regCount);
      } else {
         Iterator var2 = this.regs.iterator();

         while(var2.hasNext()) {
            Register r = (Register)var2.next();
            if (!r.fitsUnconstrained()) {
               return getAllIncompatible(regCount);
            }

            if (r.isWide()) {
               boolean secondWideHalfFits = Register.fitsUnconstrained(r.getNumber() + 1, false);
               if (!secondWideHalfFits) {
                  return getAllIncompatible(regCount);
               }
            }
         }

         return new BitSet(regCount);
      }
   }

   private static BitSet getAllIncompatible(int regCount) {
      BitSet incompatRegs = new BitSet(regCount);
      incompatRegs.flip(0, regCount);
      return incompatRegs;
   }

   private boolean hasHoleInRange() {
      Register startReg = (Register)this.regs.get(0);
      int nextExpectedRegNum = startReg.getNumber() + 1;
      if (startReg.isWide()) {
         ++nextExpectedRegNum;
      }

      for(int i = 1; i < this.regs.size(); ++i) {
         Register r = (Register)this.regs.get(i);
         int regNum = r.getNumber();
         if (regNum != nextExpectedRegNum) {
            return true;
         }

         ++nextExpectedRegNum;
         if (r.isWide()) {
            ++nextExpectedRegNum;
         }
      }

      return false;
   }

   public String toString() {
      return super.toString() + " ref: " + this.referencedItem;
   }
}
