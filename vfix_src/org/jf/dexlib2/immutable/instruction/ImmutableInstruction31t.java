package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction31t extends ImmutableInstruction implements Instruction31t {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int codeOffset;

   public ImmutableInstruction31t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.codeOffset = codeOffset;
   }

   public static ImmutableInstruction31t of(Instruction31t instruction) {
      return instruction instanceof ImmutableInstruction31t ? (ImmutableInstruction31t)instruction : new ImmutableInstruction31t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getCodeOffset());
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
      FORMAT = Format.Format31t;
   }
}
