package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22cs extends ImmutableInstruction implements Instruction22cs {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int fieldOffset;

   public ImmutableInstruction22cs(@Nonnull Opcode opcode, int registerA, int registerB, int fieldOffset) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
      this.fieldOffset = Preconditions.checkFieldOffset(fieldOffset);
   }

   public static ImmutableInstruction22cs of(Instruction22cs instruction) {
      return instruction instanceof ImmutableInstruction22cs ? (ImmutableInstruction22cs)instruction : new ImmutableInstruction22cs(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getFieldOffset());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
   }

   public int getFieldOffset() {
      return this.fieldOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format22cs;
   }
}
