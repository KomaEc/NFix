package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22b extends ImmutableInstruction implements Instruction22b {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int literal;

   public ImmutableInstruction22b(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.registerB = Preconditions.checkByteRegister(registerB);
      this.literal = Preconditions.checkByteLiteral(literal);
   }

   public static ImmutableInstruction22b of(Instruction22b instruction) {
      return instruction instanceof ImmutableInstruction22b ? (ImmutableInstruction22b)instruction : new ImmutableInstruction22b(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
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
      FORMAT = Format.Format22b;
   }
}
