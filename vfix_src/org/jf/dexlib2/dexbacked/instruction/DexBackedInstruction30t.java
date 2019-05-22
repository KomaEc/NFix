package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;

public class DexBackedInstruction30t extends DexBackedInstruction implements Instruction30t {
   public DexBackedInstruction30t(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getCodeOffset() {
      return this.dexFile.readInt(this.instructionStart + 2);
   }
}
