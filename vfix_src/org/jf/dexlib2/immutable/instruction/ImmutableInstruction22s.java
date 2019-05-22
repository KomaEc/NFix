package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22s extends ImmutableInstruction implements Instruction22s {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int literal;

   public ImmutableInstruction22s(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
      this.literal = Preconditions.checkShortLiteral(literal);
   }

   public static ImmutableInstruction22s of(Instruction22s instruction) {
      return instruction instanceof ImmutableInstruction22s ? (ImmutableInstruction22s)instruction : new ImmutableInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
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
      FORMAT = Format.Format22s;
   }
}
