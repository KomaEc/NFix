package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction51l extends BuilderInstruction implements Instruction51l {
   public static final Format FORMAT;
   protected final int registerA;
   protected final long literal;

   public BuilderInstruction51l(@Nonnull Opcode opcode, int registerA, long literal) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.literal = literal;
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
