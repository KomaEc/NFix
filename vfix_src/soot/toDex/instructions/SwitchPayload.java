package soot.toDex.instructions;

import java.util.List;
import soot.Unit;

public abstract class SwitchPayload extends AbstractPayload {
   protected Insn31t switchInsn;
   protected List<Unit> targets;

   public SwitchPayload(List<Unit> targets) {
      this.targets = targets;
   }

   public void setSwitchInsn(Insn31t switchInsn) {
      this.switchInsn = switchInsn;
   }

   public int getMaxJumpOffset() {
      return 32767;
   }
}
