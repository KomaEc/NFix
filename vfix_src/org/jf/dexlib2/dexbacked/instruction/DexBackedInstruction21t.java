package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;

public class DexBackedInstruction21t extends DexBackedInstruction implements Instruction21t {
   public DexBackedInstruction21t(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getCodeOffset() {
      return this.dexFile.readShort(this.instructionStart + 2);
   }
}
