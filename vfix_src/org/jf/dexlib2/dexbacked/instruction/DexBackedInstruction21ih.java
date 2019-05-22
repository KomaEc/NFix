package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih;

public class DexBackedInstruction21ih extends DexBackedInstruction implements Instruction21ih {
   public DexBackedInstruction21ih(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getNarrowLiteral() {
      return this.getHatLiteral() << 16;
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }

   public short getHatLiteral() {
      return (short)this.dexFile.readShort(this.instructionStart + 2);
   }
}
