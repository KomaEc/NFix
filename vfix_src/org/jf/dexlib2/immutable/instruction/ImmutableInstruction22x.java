package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22x;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22x extends ImmutableInstruction implements Instruction22x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;

   public ImmutableInstruction22x(@Nonnull Opcode opcode, int registerA, int registerB) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.registerB = Preconditions.checkShortRegister(registerB);
   }

   public static ImmutableInstruction22x of(Instruction22x instruction) {
      return instruction instanceof ImmutableInstruction22x ? (ImmutableInstruction22x)instruction : new ImmutableInstruction22x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
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
      FORMAT = Format.Format22x;
   }
}
