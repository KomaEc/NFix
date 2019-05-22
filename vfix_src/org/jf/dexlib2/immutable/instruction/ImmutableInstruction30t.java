package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;

public class ImmutableInstruction30t extends ImmutableInstruction implements Instruction30t {
   public static final Format FORMAT;
   protected final int codeOffset;

   public ImmutableInstruction30t(@Nonnull Opcode opcode, int codeOffset) {
      super(opcode);
      this.codeOffset = codeOffset;
   }

   public static ImmutableInstruction30t of(Instruction30t instruction) {
      return instruction instanceof ImmutableInstruction30t ? (ImmutableInstruction30t)instruction : new ImmutableInstruction30t(instruction.getOpcode(), instruction.getCodeOffset());
   }

   public int getCodeOffset() {
      return this.codeOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format30t;
   }
}
