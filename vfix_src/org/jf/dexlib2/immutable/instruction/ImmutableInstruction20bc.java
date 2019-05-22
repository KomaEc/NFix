package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction20bc extends ImmutableInstruction implements Instruction20bc {
   public static final Format FORMAT;
   protected final int verificationError;
   @Nonnull
   protected final ImmutableReference reference;

   public ImmutableInstruction20bc(@Nonnull Opcode opcode, int verificationError, @Nonnull Reference reference) {
      super(opcode);
      this.verificationError = Preconditions.checkVerificationError(verificationError);
      this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
   }

   public static ImmutableInstruction20bc of(Instruction20bc instruction) {
      return instruction instanceof ImmutableInstruction20bc ? (ImmutableInstruction20bc)instruction : new ImmutableInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
   }

   public int getVerificationError() {
      return this.verificationError;
   }

   @Nonnull
   public ImmutableReference getReference() {
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
