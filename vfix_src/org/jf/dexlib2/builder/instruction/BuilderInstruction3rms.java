package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction3rms extends BuilderInstruction implements Instruction3rms {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   protected final int vtableIndex;

   public BuilderInstruction3rms(@Nonnull Opcode opcode, int startRegister, int registerCount, int vtableIndex) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.vtableIndex = vtableIndex;
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
