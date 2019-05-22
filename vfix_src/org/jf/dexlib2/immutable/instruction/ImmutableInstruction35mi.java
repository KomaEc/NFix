package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction35mi extends ImmutableInstruction implements Instruction35mi {
   public static final Format FORMAT;
   protected final int registerCount;
   protected final int registerC;
   protected final int registerD;
   protected final int registerE;
   protected final int registerF;
   protected final int registerG;
   protected final int inlineIndex;

   public ImmutableInstruction35mi(@Nonnull Opcode opcode, int registerCount, int registerC, int registerD, int registerE, int registerF, int registerG, int inlineIndex) {
      super(opcode);
      this.registerCount = Preconditions.check35cAnd45ccRegisterCount(registerCount);
      this.registerC = registerCount > 0 ? Preconditions.checkNibbleRegister(registerC) : 0;
      this.registerD = registerCount > 1 ? Preconditions.checkNibbleRegister(registerD) : 0;
      this.registerE = registerCount > 2 ? Preconditions.checkNibbleRegister(registerE) : 0;
      this.registerF = registerCount > 3 ? Preconditions.checkNibbleRegister(registerF) : 0;
      this.registerG = registerCount > 4 ? Preconditions.checkNibbleRegister(registerG) : 0;
      this.inlineIndex = Preconditions.checkInlineIndex(inlineIndex);
   }

   public static ImmutableInstruction35mi of(Instruction35mi instruction) {
      return instruction instanceof ImmutableInstruction35mi ? (ImmutableInstruction35mi)instruction : new ImmutableInstruction35mi(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getInlineIndex());
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getRegisterC() {
      return this.registerC;
   }

   public int getRegisterD() {
      return this.registerD;
   }

   public int getRegisterE() {
      return this.registerE;
   }

   public int getRegisterF() {
      return this.registerF;
   }

   public int getRegisterG() {
      return this.registerG;
   }

   public int getInlineIndex() {
      return this.inlineIndex;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format35mi;
   }
}
