package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.util.Preconditions;

public abstract class BuilderInstruction implements Instruction {
   @Nonnull
   protected final Opcode opcode;
   @Nullable
   MethodLocation location;

   protected BuilderInstruction(@Nonnull Opcode opcode) {
      Preconditions.checkFormat(opcode, this.getFormat());
      this.opcode = opcode;
   }

   @Nonnull
   public Opcode getOpcode() {
      return this.opcode;
   }

   public abstract Format getFormat();

   public int getCodeUnits() {
      return this.getFormat().size / 2;
   }

   @Nonnull
   public MethodLocation getLocation() {
      if (this.location == null) {
         throw new IllegalStateException("Cannot get the location of an instruction that hasn't been added to a method.");
      } else {
         return this.location;
      }
   }
}
