package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction12x extends ImmutableInstruction implements Instruction12x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;

   public ImmutableInstruction12x(@Nonnull Opcode opcode, int registerA, int registerB) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
   }

   public static ImmutableInstruction12x of(Instruction12x instruction) {
      return instruction instanceof ImmutableInstruction12x ? (ImmutableInstruction12x)instruction : new ImmutableInstruction12x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format12x;
   }
}
