package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.util.Preconditions;

public class BuilderInstruction3rmi extends BuilderInstruction implements Instruction3rmi {
   public static final Format FORMAT;
   protected final int startRegister;
   protected final int registerCount;
   protected final int inlineIndex;

   public BuilderInstruction3rmi(@Nonnull Opcode opcode, int startRegister, int registerCount, int inlineIndex) {
      super(opcode);
      this.startRegister = Preconditions.checkShortRegister(startRegister);
      this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
      this.inlineIndex = inlineIndex;
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
