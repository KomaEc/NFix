package org.jf.dexlib2.analysis;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;

public class UnresolvedOdexInstruction implements Instruction {
   public final Instruction originalInstruction;
   public final int objectRegisterNum;

   public UnresolvedOdexInstruction(Instruction originalInstruction, int objectRegisterNumber) {
      this.originalInstruction = originalInstruction;
      this.objectRegisterNum = objectRegisterNumber;
   }

   public Opcode getOpcode() {
      return this.originalInstruction.getOpcode();
   }

   public int getCodeUnits() {
      return this.originalInstruction.getCodeUnits();
   }
}
