package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.reference.Reference;

public class DexBackedInstruction20bc extends DexBackedInstruction implements Instruction20bc {
   public DexBackedInstruction20bc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getVerificationError() {
      return this.dexFile.readUbyte(this.instructionStart + 1) & 63;
   }

   @Nonnull
   public Reference getReference() {
      int referenceType = this.getReferenceType();
      return DexBackedReference.makeReference(this.dexFile, referenceType, this.dexFile.readUshort(this.instructionStart + 2));
   }

   public int getReferenceType() {
      int referenceType = (this.dexFile.readUbyte(this.instructionStart + 1) >>> 6) + 1;
      ReferenceType.validateReferenceType(referenceType);
      return referenceType;
   }
}
