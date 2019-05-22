package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction32x extends BuilderInstruction implements Instruction32x {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;

   public BuilderInstruction32x(@Nonnull Opcode opcode, int registerA, int registerB) {
      super(opcode);
      this.registerA = Preconditions.checkShortRegister(registerA);
      this.registerB = Preconditions.checkShortRegister(registerB);
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
