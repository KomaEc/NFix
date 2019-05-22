package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;

public abstract class AbstractPayload extends InsnWithOffset {
   public AbstractPayload() {
      super(Opcode.NOP);
   }
}
