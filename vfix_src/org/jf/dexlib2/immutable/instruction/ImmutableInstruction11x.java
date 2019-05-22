package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction11x extends ImmutableInstruction implements Instruction11x {
   public static final Format FORMAT;
   protected final int registerA;

   public ImmutableInstruction11x(@Nonnull Opcode opcode, int registerA) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
   }

   public static ImmutableInstruction11x of(Instruction11x instruction) {
      return instruction instanceof ImmutableInstruction11x ? (ImmutableInstruction11x)instruction : new ImmutableInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
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
