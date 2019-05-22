package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.util.NibbleUtils;

public class DexBackedInstruction22c extends DexBackedInstruction implements Instruction22c {
   public DexBackedInstruction22c(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return NibbleUtils.extractLowUnsignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   public int getRegisterB() {
      return NibbleUtils.extractHighUnsignedNibble(this.dexFile.readByte(this.instructionStart + 1));
   }

   @Nonnull
   public Reference getReference() {
      return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType, this.dexFile.readUshort(this.instructionStart + 2));
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }
}
