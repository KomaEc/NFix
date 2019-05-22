package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;

public class DexBackedInstruction10t extends DexBackedInstruction implements Instruction10t {
   public DexBackedInstruction10t(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getCodeOffset() {
      return this.dexFile.readByte(this.instructionStart + 1);
   }
}
