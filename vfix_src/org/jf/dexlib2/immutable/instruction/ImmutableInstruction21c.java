package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction21c extends ImmutableInstruction implements Instruction21c {
   public static final Format FORMAT;
   protected final int registerA;
   @Nonnull
   protected final ImmutableReference reference;

   public ImmutableInstruction21c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
   }

   public static ImmutableInstruction21c of(Instruction21c instruction) {
      return instruction instanceof ImmutableInstruction21c ? (ImmutableInstruction21c)instruction : new ImmutableInstruction21c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   @Nonnull
   public ImmutableReference getReference() {
      return this.reference;
   }

   public int getReferenceType() {
      return this.opcode.referenceType;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format21c;
   }
}
