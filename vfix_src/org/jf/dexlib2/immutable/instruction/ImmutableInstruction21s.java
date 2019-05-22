package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21s;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction21s extends ImmutableInstruction implements Instruction21s {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int literal;

   public ImmutableInstruction21s(@Nonnull Opcode opcode, int registerA, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = Preconditions.checkShortLiteral(literal);
   }

   public static ImmutableInstruction21s of(Instruction21s instruction) {
      return instruction instanceof ImmutableInstruction21s ? (ImmutableInstruction21s)instruction : new ImmutableInstruction21s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getNarrowLiteral() {
      return this.literal;
   }

   public long getWideLiteral() {
      return (long)this.literal;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format21s;
   }
}
