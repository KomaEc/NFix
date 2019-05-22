package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction22t extends ImmutableInstruction implements Instruction22t {
   public static final Format FORMAT;
   protected final int registerA;
   protected final int registerB;
   protected final int codeOffset;

   public ImmutableInstruction22t(@Nonnull Opcode opcode, int registerA, int registerB, int codeOffset) {
      super(opcode);
      this.registerA = Preconditions.checkNibbleRegister(registerA);
      this.registerB = Preconditions.checkNibbleRegister(registerB);
      this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
   }

   public static ImmutableInstruction22t of(Instruction22t instruction) {
      return instruction instanceof ImmutableInstruction22t ? (ImmutableInstruction22t)instruction : new ImmutableInstruction22t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getCodeOffset());
   }

   public int getRegisterA() {
      return this.registerA;
   }

   public int getRegisterB() {
      return this.registerB;
   }

   public int getCodeOffset() {
      return this.codeOffset;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format22t;
   }
}
