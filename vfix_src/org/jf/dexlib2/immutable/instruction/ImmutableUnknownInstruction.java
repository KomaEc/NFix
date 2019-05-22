package org.jf.dexlib2.immutable.instruction;

import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.UnknownInstruction;

public class ImmutableUnknownInstruction extends ImmutableInstruction implements UnknownInstruction {
   public static final Format FORMAT;
   protected final int originalOpcode;

   public ImmutableUnknownInstruction(int originalOpcode) {
      super(Opcode.NOP);
      this.originalOpcode = originalOpcode;
   }

   public static ImmutableUnknownInstruction of(UnknownInstruction instruction) {
      return instruction instanceof ImmutableUnknownInstruction ? (ImmutableUnknownInstruction)instruction : new ImmutableUnknownInstruction(instruction.getOriginalOpcode());
   }

   public Format getFormat() {
      return FORMAT;
   }

   public int getOriginalOpcode() {
      return this.originalOpcode;
   }

   static {
      FORMAT = Format.Format10x;
   }
}
