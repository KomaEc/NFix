package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;

public class DexBackedInstruction3rms extends DexBackedInstruction implements Instruction3rms {
   public DexBackedInstruction3rms(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterCount() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getStartRegister() {
      return this.dexFile.readUshort(this.instructionStart + 4);
   }

   public int getVtableIndex() {
      return this.dexFile.readUshort(this.instructionStart + 2);
   }
}
