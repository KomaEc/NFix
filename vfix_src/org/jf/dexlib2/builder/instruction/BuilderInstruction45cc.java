package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction45cc extends BuilderInstruction implements Instruction45cc {
   public static final Format FORMAT;
   protected final int registerCount;
   protected final int registerC;
   protected final int registerD;
   protected final int registerE;
   protected final int registerF;
   protected final int registerG;
   @Nonnull
   protected final Reference reference;
   @Nonnull
   protected final Reference reference2;

   public BuilderInstruction45cc(@Nonnull Opcode opcode, int registerCount, int registerC, int registerD, int registerE, int registerF, int registerG, @Nonnull Reference reference, @Nonnull Reference reference2) {
      super(opcode);
      this.registerCount = Preconditions.check35cAnd45ccRegisterCount(registerCount);
      this.registerC = registerCount > 0 ? Preconditions.checkNibbleRegister(registerC) : 0;
      this.registerD = registerCount > 1 ? Preconditions.checkNibbleRegister(registerD) : 0;
      this.registerE = registerCount > 2 ? Preconditions.checkNibbleRegister(registerE) : 0;
      this.registerF = registerCount > 3 ? Preconditions.checkNibbleRegister(registerF) : 0;
      this.registerG = registerCount > 4 ? Preconditions.checkNibbleRegister(registerG) : 0;
      this.reference = reference;
      this.reference2 = reference2;
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
      FORMAT = Format.Format45cc;
   }
}
