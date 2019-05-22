package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction21lh extends BuilderInstruction implements Instruction21lh {
   public static final Format FORMAT;
   protected final int registerA;
   protected final long literal;

   public BuilderInstruction21lh(@Nonnull Opcode opcode, int registerA, long literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = Preconditions.checkLongHatLiteral(literal);
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
