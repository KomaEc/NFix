package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction31i;

public class DexBackedInstruction31i extends DexBackedInstruction implements Instruction31i {
   public DexBackedInstruction31i(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getNarrowLiteral() {
      return this.dexFile.readInt(this.instructionStart + 2);
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }
}
