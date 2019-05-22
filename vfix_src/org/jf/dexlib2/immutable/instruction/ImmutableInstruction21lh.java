package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction21lh extends ImmutableInstruction implements Instruction21lh {
   public static final Format FORMAT;
   protected final int registerA;
   protected final long literal;

   public ImmutableInstruction21lh(@Nonnull Opcode opcode, int registerA, long literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = Preconditions.checkLongHatLiteral(literal);
   }

   public static ImmutableInstruction21lh of(Instruction21lh instruction) {
      return instruction instanceof ImmutableInstruction21lh ? (ImmutableInstruction21lh)instruction : new ImmutableInstruction21lh(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public long getWideLiteral() {
      return this.literal;
   }

   public short getHatLiteral() {
      return (short)((int)(this.literal >>> 48));
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format21lh;
   }
}
