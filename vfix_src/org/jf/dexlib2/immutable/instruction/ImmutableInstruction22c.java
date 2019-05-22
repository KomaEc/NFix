package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22c extends ImmutableInstruction implements Instruction22c {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   @Nonnull
   protected final ImmutableReference reference;

   public ImmutableInstruction22c(@Nonnull Opcode opcode, int registerA, int registerB, @Nonnull Reference reference) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
      this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
   }

   public static ImmutableInstruction22c of(Instruction22c instruction) {
      return instruction instanceof ImmutableInstruction22c ? (ImmutableInstruction22c)instruction : new ImmutableInstruction22c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getReference());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
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
      FORMAT = Format.Format22c;
   }
}
