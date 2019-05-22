package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction23x extends BuilderInstruction implements Instruction23x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int registerC;

   public BuilderInstruction23x(@Nonnull Opcode opcode, int registerA, int registerB, int registerC) {
      super(opcode);
      this.registerA = Preconditions.checkByteRegister(registerA);
      this.registerB = Preconditions.checkByteRegister(registerB);
      this.registerC = Preconditions.checkByteRegister(registerC);
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
