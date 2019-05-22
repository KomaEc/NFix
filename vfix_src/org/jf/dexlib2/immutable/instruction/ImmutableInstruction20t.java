package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction20t extends ImmutableInstruction implements Instruction20t {
   public static final Format FORMAT;
   protected final int codeOffset;

   public ImmutableInstruction20t(@Nonnull Opcode opcode, int codeOffset) {
      super(opcode);
      this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
   }

   public static ImmutableInstruction20t of(Instruction20t instruction) {
      return instruction instanceof ImmutableInstruction20t ? (ImmutableInstruction20t)instruction : new ImmutableInstruction20t(instruction.getOpcode(), instruction.getCodeOffset());
   }

   public int getCodeOffset() {
      return this.codeOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format20t;
   }
}
