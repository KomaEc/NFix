package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction10t extends ImmutableInstruction implements Instruction10t {
   public static final Format FORMAT;
   protected final int codeOffset;

   public ImmutableInstruction10t(@Nonnull Opcode opcode, int codeOffset) {
      super(opcode);
      this.codeOffset = Preconditions.checkByteCodeOffset(codeOffset);
   }

   public static ImmutableInstruction10t of(Instruction10t instruction) {
      return instruction instanceof ImmutableInstruction10t ? (ImmutableInstruction10t)instruction : new ImmutableInstruction10t(instruction.getOpcode(), instruction.getCodeOffset());
   }

   public int getCodeOffset() {
      return this.codeOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format10t;
   }
}
