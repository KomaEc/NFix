package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction3rc extends ImmutableInstruction implements Instruction3rc {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   @Nonnull
   protected final ImmutableReference reference;

   public ImmutableInstruction3rc(@Nonnull Opcode opcode, int startRegister, int registerCount, @Nonnull Reference reference) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
   }

   public static ImmutableInstruction3rc of(Instruction3rc instruction) {
      return instruction instanceof ImmutableInstruction3rc ? (ImmutableInstruction3rc)instruction : new ImmutableInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getReference());
   }

   public int getStartRegister() {
      return this.startRegister;
   }

   public int getRegisterCount() {
      return this.registerCount;
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
      FORMAT = Format.Format3rc;
   }
}
