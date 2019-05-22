package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;

public class ImmutableInstruction10x extends ImmutableInstruction implements Instruction10x {
   public static final Format FORMAT;

   public ImmutableInstruction10x(@Nonnull Opcode opcode) {
      super(opcode);
   }

   public static ImmutableInstruction10x of(Instruction10x instruction) {
      return instruction instanceof ImmutableInstruction10x ? (ImmutableInstruction10x)instruction : new ImmutableInstruction10x(instruction.getOpcode());
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format10x;
   }
}
