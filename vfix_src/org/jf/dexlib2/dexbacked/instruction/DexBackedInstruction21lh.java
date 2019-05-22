package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;

public class DexBackedInstruction21lh extends DexBackedInstruction implements Instruction21lh {
   public DexBackedInstruction21lh(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public long getWideLiteral() {
      return (long)this.getHatLiteral() << 48;
   }

   public short getHatLiteral() {
      return (short)this.dexFile.readShort(this.instructionStart + 2);
   }
}
