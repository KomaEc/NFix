package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;

public class BuilderInstruction10x extends BuilderInstruction implements Instruction10x {
   public static final Format FORMAT;

   public BuilderInstruction10x(@Nonnull Opcode opcode) {
      super(opcode);
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format10x;
   }
}
