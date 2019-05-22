package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.util.ExceptionWithContext;

public abstract class BuilderOffsetInstruction extends BuilderInstruction implements OffsetInstruction {
   @Nonnull
   protected final Label target;

   public BuilderOffsetInstruction(@Nonnull Opcode opcode, @Nonnull Label target) {
      super(opcode);
      this.target = target;
   }

   public int getCodeOffset() {
      int codeOffset = this.internalGetCodeOffset();
      if (this.getCodeUnits() == 1) {
         if (codeOffset < -128 || codeOffset > 127) {
            throw new ExceptionWithContext("Invalid instruction offset: %d. Offset must be in [-128, 127]", new Object[]{codeOffset});
         }
      } else if (this.getCodeUnits() == 2 && (codeOffset < -32768 || codeOffset > 32767)) {
         throw new ExceptionWithContext("Invalid instruction offset: %d. Offset must be in [-32768, 32767]", new Object[]{codeOffset});
      }

      return codeOffset;
   }

   int internalGetCodeOffset() {
      return this.target.getCodeAddress() - this.getLocation().getCodeAddress();
   }

   @Nonnull
   public Label getTarget() {
      return this.target;
   }
}
