package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction3rc extends BuilderInstruction implements Instruction3rc {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   @Nonnull
   protected final Reference reference;

   public BuilderInstruction3rc(@Nonnull Opcode opcode, int startRegister, int registerCount, @Nonnull Reference reference) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.reference = reference;
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

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format3rc;
   }
}
