package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.builder.BuilderSwitchPayload;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.SwitchElement;

public class BuilderSwitchElement implements SwitchElement {
   @Nonnull
   BuilderSwitchPayload parent;
   private final int key;
   @Nonnull
   private final Label target;

   public BuilderSwitchElement(@Nonnull BuilderSwitchPayload parent, int key, @Nonnull Label target) {
      this.parent = parent;
      this.key = key;
      this.target = target;
   }

   public int getKey() {
      return this.key;
   }

   public int getOffset() {
      return this.target.getCodeAddress() - this.parent.getReferrer().getCodeAddress();
   }

   @Nonnull
   public Label getTarget() {
      return this.target;
   }
}
