package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction21c extends BuilderInstruction implements Instruction21c {
   public static final Format FORMAT;
   protected final int registerA;
   @Nonnull
   protected final Reference reference;

   public BuilderInstruction21c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.reference = reference;
   }

   public int getRegisterA() {
      return this.registerA;
   }

   @Nonnull
   public Reference getReference() {
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
