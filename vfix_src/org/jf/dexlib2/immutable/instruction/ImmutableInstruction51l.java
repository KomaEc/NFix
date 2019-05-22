package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction51l extends ImmutableInstruction implements Instruction51l {
   public static final Format FORMAT;
   protected final int registerA;
   protected final long literal;

   public ImmutableInstruction51l(@Nonnull Opcode opcode, int registerA, long literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = literal;
   }

   public static ImmutableInstruction51l of(Instruction51l instruction) {
      return instruction instanceof ImmutableInstruction51l ? (ImmutableInstruction51l)instruction : new ImmutableInstruction51l(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public long getWideLiteral() {
      return this.literal;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format51l;
   }
}
