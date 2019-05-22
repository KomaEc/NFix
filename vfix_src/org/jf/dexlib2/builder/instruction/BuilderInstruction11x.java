package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction11x extends BuilderInstruction implements Instruction11x {
   public static final Format FORMAT;
   protected final int registerA;

   public BuilderInstruction11x(@Nonnull Opcode opcode, int registerA) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format11x;
   }
}
