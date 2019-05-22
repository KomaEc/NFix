package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.reference.Reference;

public class DexBackedInstruction4rcc extends DexBackedInstruction implements Instruction4rcc {
   public DexBackedInstruction4rcc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
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

   public Reference getReference2() {
      return DexBackedReference.makeReference(this.dexFile, this.opcode.referenceType2, this.dexFile.readUshort(this.instructionStart + 3));
   }

   public int getReferenceType2() {
      return this.opcode.referenceType2;
   }
}
