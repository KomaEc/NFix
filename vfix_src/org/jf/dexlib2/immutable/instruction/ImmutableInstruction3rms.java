package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.util.Preconditions;

public class ImmutableInstruction3rms extends ImmutableInstruction implements Instruction3rms {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   protected final int vtableIndex;

   public ImmutableInstruction3rms(@Nonnull Opcode opcode, int startRegister, int registerCount, int vtableIndex) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.vtableIndex = Preconditions.checkVtableIndex(vtableIndex);
   }

   public static ImmutableInstruction3rms of(Instruction3rms instruction) {
      return instruction instanceof ImmutableInstruction3rms ? (ImmutableInstruction3rms)instruction : new ImmutableInstruction3rms(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getVtableIndex());
   }

   public int getStartRegister() {
      return this.startRegister;
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   public int getVtableIndex() {
      return this.vtableIndex;
   }

   public Format getFormat() {
      return FORMAT;
   }

   static {
      FORMAT = Format.Format3rms;
   }
}
