package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.util.NibbleUtils;

public class DexBackedInstruction35ms extends DexBackedInstruction implements Instruction35ms {
   public DexBackedInstruction35ms(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterCount() {
      return NibbleUtils.extractHighUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 1));
   }

   public int getRegisterC() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 4));
   }

   public int getRegisterD() {
      return NibbleUtils.extractHighUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 4));
   }

   public int getRegisterE() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 5));
   }

   public int getRegisterF() {
      return NibbleUtils.extractHighUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 5));
   }

   public int getRegisterG() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readUbyte(this.instructionStart + 1));
   }

   public int getVtableIndex() {
      return this.dexFile.readUshort(this.instructionStart + 2);
   }
}
