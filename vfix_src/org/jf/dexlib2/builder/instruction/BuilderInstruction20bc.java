package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction20bc extends BuilderInstruction implements Instruction20bc {
   public static final Format FORMAT;
   protected final int verificationError;
   @Nonnull
   protected final Reference reference;

   public BuilderInstruction20bc(@Nonnull Opcode opcode, int verificationError, @Nonnull Reference reference) {
      super(opcode);
      this.verificationError = Preconditions.checkVerificationError(verificationError);
      this.reference = reference;
   }

   public int getVerificationError() {
      return this.verificationError;
   }

   @Nonnull
   public Reference getReference() {
      return this.reference;
   }

   public int getReferenceType() {
      return ReferenceType.getReferenceType(this.reference);
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format20bc;
   }
}
