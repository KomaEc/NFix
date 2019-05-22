package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.util.NibbleUtils;

public class DexBackedInstruction22s extends DexBackedInstruction implements Instruction22s {
   public DexBackedInstruction22s(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   public int getRegisterB() {
      return NibbleUtils.extractHighUnsignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   public int getNarrowLiteral() {
      return this.dexFile.readShort(this.instructionStart + 2);
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }
}
