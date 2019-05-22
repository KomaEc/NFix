package org.jf.dexlib2.builder;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.instruction.BuilderSwitchElement;
import org.jf.dexlib2.iface.instruction.SwitchPayload;

public abstract class BuilderSwitchPayload extends BuilderInstruction implements SwitchPayload {
   @Nullable
   MethodLocation referrer;

   protected BuilderSwitchPayload(@Nonnull Opcode opcode) {
      super(opcode);
   }

   @Nonnull
   public MethodLocation getReferrer() {
      if (this.referrer == null) {
         throw new IllegalStateException("The referrer has not been set yet");
      } else {
         return this.referrer;
      }
   }

   @Nonnull
   public abstract List<? extends BuilderSwitchElement> getSwitchElements();
}
