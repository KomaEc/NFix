package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;

public class DexBackedInstruction3rmi extends DexBackedInstruction implements Instruction3rmi {
   public DexBackedInstruction3rmi(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterCount() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getStartRegister() {
      return this.dexFile.readUshort(this.instructionStart + 4);
   }

   public int getInlineIndex() {
      return this.dexFile.readUshort(this.instructionStart + 2);
   }
}
