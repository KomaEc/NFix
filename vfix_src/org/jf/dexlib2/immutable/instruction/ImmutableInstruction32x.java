package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction32x extends ImmutableInstruction implements Instruction32x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;

   public ImmutableInstruction32x(@Nonnull Opcode opcode, int registerA, int registerB) {
      super(opcode);
      this.registerA = Preconditions.checkShortRegister(registerA);
      this.registerB = Preconditions.checkShortRegister(registerB);
   }

   public static ImmutableInstruction32x of(Instruction32x instruction) {
      return instruction instanceof ImmutableInstruction32x ? (ImmutableInstruction32x)instruction : new ImmutableInstruction32x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
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
      FORMAT = Format.Format32x;
   }
}
