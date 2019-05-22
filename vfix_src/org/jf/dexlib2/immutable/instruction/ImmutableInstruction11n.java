package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction11n extends ImmutableInstruction implements Instruction11n {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int literal;

   public ImmutableInstruction11n(@Nonnull Opcode opcode, int registerA, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.literal = Preconditions.checkNibbleLiteral(literal);
   }

   public static ImmutableInstruction11n of(Instruction11n instruction) {
      return instruction instanceof ImmutableInstruction11n ? (ImmutableInstruction11n)instruction : new ImmutableInstruction11n(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
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
      FORMAT = Format.Format11n;
   }
}
