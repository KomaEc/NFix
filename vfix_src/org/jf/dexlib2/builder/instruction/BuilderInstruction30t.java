package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;

public class BuilderInstruction30t extends BuilderOffsetInstruction implements Instruction30t {
   public static final Format FORMAT;

   public BuilderInstruction30t(@Nonnull Opcode opcode, @Nonnull Label target) {
      super(opcode, target);
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format30t;
   }
}
