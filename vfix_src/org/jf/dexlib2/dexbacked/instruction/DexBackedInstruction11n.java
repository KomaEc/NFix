package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.util.NibbleUtils;

public class DexBackedInstruction11n extends DexBackedInstruction implements Instruction11n {
   public DexBackedInstruction11n(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   public int getNarrowLiteral() {
      return NibbleUtils.extractHighSignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   public long getWideLiteral() {
      return (long)this.getNarrowLiteral();
   }
}
