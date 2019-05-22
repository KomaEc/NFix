package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction23x extends ImmutableInstruction implements Instruction23x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int registerC;

   public ImmutableInstruction23x(@Nonnull Opcode opcode, int registerA, int registerB, int registerC) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.registerB = Preconditions.checkByteRegister(registerB);
      this.registerC = Preconditions.checkByteRegister(registerC);
   }

   public static ImmutableInstruction23x of(Instruction23x instruction) {
      return instruction instanceof ImmutableInstruction23x ? (ImmutableInstruction23x)instruction : new ImmutableInstruction23x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getRegisterC());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
   }

   public int getRegisterC() {
      return this.registerC;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format23x;
   }
}
