package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.util.NibbleUtils;

public class DexBackedInstruction35c extends DexBackedInstruction implements Instruction35c {
   public DexBackedInstruction35c(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
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

   @Nonnull
   public Reference getReference() {
      return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType, this.dexFile.readUshort(this.instructionStart + 2));
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }
}
