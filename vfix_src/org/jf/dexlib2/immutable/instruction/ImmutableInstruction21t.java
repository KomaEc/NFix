package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction21t extends ImmutableInstruction implements Instruction21t {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int codeOffset;

   public ImmutableInstruction21t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
   }

   public static ImmutableInstruction21t of(Instruction21t instruction) {
      return instruction instanceof ImmutableInstruction21t ? (ImmutableInstruction21t)instruction : new ImmutableInstruction21t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getCodeOffset());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getCodeOffset() {
      return this.codeOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format21t;
   }
}
