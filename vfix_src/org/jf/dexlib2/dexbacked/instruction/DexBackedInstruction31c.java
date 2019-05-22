package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.reference.Reference;

public class DexBackedInstruction31c extends DexBackedInstruction implements Instruction31c {
   public DexBackedInstruction31c(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
      super(dexFile, opcode, instructionStart);
   }

   public int getRegisterA() {
      return this.dexFile.readUbyte(this.instructionStart + 1);
   }

   @Nonnull
   public Reference getReference() {
      return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType, this.dexFile.readSmallUint(this.instructionStart + 2));
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }
}
