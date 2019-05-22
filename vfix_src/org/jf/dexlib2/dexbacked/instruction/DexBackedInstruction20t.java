package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t;

public class DexBackedInstruction20t extends DexBackedInstruction implements Instruction20t {
   public DexBackedInstruction20t(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getCodeOffset() {
      return this.dexFile.readShort(this.instructionStart + 2);
   }
}
