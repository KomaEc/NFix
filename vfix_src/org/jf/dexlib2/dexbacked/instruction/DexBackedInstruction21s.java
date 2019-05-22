package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction21s;

public class DexBackedInstruction21s extends DexBackedInstruction implements Instruction21s {
   public DexBackedInstruction21s(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getNarrowLiteral() {
      return this.dexFile.readShort(this.instructionStart + 2);
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }
}
