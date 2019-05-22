package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction35c extends ImmutableInstruction implements Instruction35c {
   public static final Format FORMAT;
   protected final int registerCount;
   protected final int registerC;
   protected final int registerD;
   protected final int registerE;
   protected final int registerF;
   protected final int registerG;
   @Nonnull
   protected final ImmutableReference reference;

   public ImmutableInstruction35c(@Nonnull Opcode opcode, int registerCount, int registerC, int registerD, int registerE, int registerF, int registerG, @Nonnull Reference reference) {
      super(opcode);
      this.registerCount = Preconditions.check35cAnd45ccRegisterCount(registerCount);
      this.registerC = registerCount > 0 ? Preconditions.checkNibbleRegister(registerC) : 0;
      this.registerD = registerCount > 1 ? Preconditions.checkNibbleRegister(registerD) : 0;
      this.registerE = registerCount > 2 ? Preconditions.checkNibbleRegister(registerE) : 0;
      this.registerF = registerCount > 3 ? Preconditions.checkNibbleRegister(registerF) : 0;
      this.registerG = registerCount > 4 ? Preconditions.checkNibbleRegister(registerG) : 0;
      this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
   }

   public static ImmutableInstruction35c of(Instruction35c instruction) {
      return instruction instanceof ImmutableInstruction35c ? (ImmutableInstruction35c)instruction : new ImmutableInstruction35c(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getRegisterC() {
      return this.registerC;
   }

   public int getRegisterD() {
      return this.registerD;
   }

   public int getRegisterE() {
      return this.registerE;
   }

   public int getRegisterF() {
      return this.registerF;
   }

   public int getRegisterG() {
      return this.registerG;
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
      FORMAT = Format.Format35c;
   }
}
