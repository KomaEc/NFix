package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.UnknownInstruction;

public class DexBackedUnknownInstruction extends DexBackedInstruction implements UnknownInstruction {
   public DexBackedUnknownInstruction(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
      super(dexFile, Opcode.NOP, instructionStart);
   }

   public int getOriginalOpcode() {
      int opcode = this.dexFile.readUbyte(this.instructionStart);
      if (opcode == 0) {
         opcode = this.dexFile.readUshort(this.instructionStart);
      }

      return opcode;
   }
}
