package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;

public class DexBackedInstruction23x extends DexBackedInstruction implements Instruction23x {
   public DexBackedInstruction23x(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getRegisterB() {
      return this.dexFile.readUbyte(this.instructionStart + 2);
   }

   public int getRegisterC() {
      return this.dexFile.readUbyte(this.instructionStart + 3);
   }
}
