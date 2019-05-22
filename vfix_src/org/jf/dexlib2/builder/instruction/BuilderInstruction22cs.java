package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction22cs extends BuilderInstruction implements Instruction22cs {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int fieldOffset;

   public BuilderInstruction22cs(@Nonnull Opcode opcode, int registerA, int registerB, int fieldOffset) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
      this.fieldOffset = fieldOffset;
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
