package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction4rcc extends BuilderInstruction implements Instruction4rcc {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   @Nonnull
   protected final Reference reference;
   @Nonnull
   protected final Reference reference2;

   public BuilderInstruction4rcc(@Nonnull Opcode opcode, int startRegister, int registerCount, @Nonnull Reference reference, @Nonnull Reference reference2) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.reference = reference;
      this.reference2 = reference2;
   }

   public int getStartRegister() {
      return this.startRegister;
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   @Nonnull
   public Reference getReference() {
      return this.reference;
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }

   @Nonnull
   public Reference getReference2() {
      return this.reference2;
   }

   public int getReferenceType2() {
      return this.opcode.referenceType2;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format4rcc;
   }
}
