package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction3rmi extends ImmutableInstruction implements Instruction3rmi {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   protected final int inlineIndex;

   public ImmutableInstruction3rmi(@Nonnull Opcode opcode, int startRegister, int registerCount, int inlineIndex) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.inlineIndex = Preconditions.checkInlineIndex(inlineIndex);
   }

   public static ImmutableInstruction3rmi of(Instruction3rmi instruction) {
      return instruction instanceof ImmutableInstruction3rmi ? (ImmutableInstruction3rmi)instruction : new ImmutableInstruction3rmi(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getInlineIndex());
   }

   public int getStartRegister() {
      return this.startRegister;
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getInlineIndex() {
      return this.inlineIndex;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format3rmi;
   }
}
