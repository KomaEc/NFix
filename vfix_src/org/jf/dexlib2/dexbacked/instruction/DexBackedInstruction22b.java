package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;

public class DexBackedInstruction22b extends DexBackedInstruction implements Instruction22b {
   public DexBackedInstruction22b(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getRegisterB() {
      return this.dexFile.readUbyte(this.instructionStart + 2);
   }

   public int getNarrowLiteral() {
      return this.dexFile.readByte(this.instructionStart + 3);
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }
}
