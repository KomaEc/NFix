package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction31t extends BuilderOffsetInstruction implements Instruction31t {
   public static final Format FORMAT;
   protected final int registerA;

   public BuilderInstruction31t(@Nonnull Opcode opcode, int registerA, @Nonnull Label target) {
      super(opcode, target);
      this.registerA = Preconditions.checkByteRegister(registerA);
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format31t;
   }
}
