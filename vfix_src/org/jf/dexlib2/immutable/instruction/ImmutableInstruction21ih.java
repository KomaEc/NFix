package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction21ih extends ImmutableInstruction implements Instruction21ih {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int literal;

   public ImmutableInstruction21ih(@Nonnull Opcode opcode, int registerA, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = Preconditions.checkIntegerHatLiteral(literal);
   }

   public static ImmutableInstruction21ih of(Instruction21ih instruction) {
      return instruction instanceof ImmutableInstruction21ih ? (ImmutableInstruction21ih)instruction : new ImmutableInstruction21ih(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
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

   public short getHatLiteral() {
      return (short)(this.literal >>> 16);
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format21ih;
   }
}
