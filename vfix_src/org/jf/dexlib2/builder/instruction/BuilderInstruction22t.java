package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction22t extends BuilderOffsetInstruction implements Instruction22t {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;

   public BuilderInstruction22t(@Nonnull Opcode opcode, int registerA, int registerB, @Nonnull Label target) {
      super(opcode, target);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
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
      FORMAT = Format.Format22t;
   }
}
