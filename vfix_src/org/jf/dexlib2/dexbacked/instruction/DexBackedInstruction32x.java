package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;

public class DexBackedInstruction32x extends DexBackedInstruction implements Instruction32x {
   public DexBackedInstruction32x(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUshort(this.instructionStart + 2);
   }

   public int getRegisterB() {
      return this.dexFile.readUshort(this.instructionStart + 4);
   }
}
