package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction31i;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction31i extends BuilderInstruction implements Instruction31i {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int literal;

   public BuilderInstruction31i(@Nonnull Opcode opcode, int registerA, int literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = literal;
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
      FORMAT = Format.Format31i;
   }
}
