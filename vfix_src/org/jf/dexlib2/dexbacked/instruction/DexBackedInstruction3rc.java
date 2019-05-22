package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.Reference;

public class DexBackedInstruction3rc extends DexBackedInstruction implements Instruction3rc {
   public DexBackedInstruction3rc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterCount() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   public int getStartRegister() {
      return this.dexFile.readUshort(this.instructionStart + 4);
   }

   @Nonnull
   public Reference getReference() {
      return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType, this.dexFile.readUshort(this.instructionStart + 2));
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }
}
