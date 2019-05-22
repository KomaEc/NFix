package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import soot.toDex.LabelAssigner;

public class AddressInsn extends AbstractInsn {
   private Object originalSource;

   public AddressInsn(Object originalSource) {
      super(Opcode.NOP);
      this.originalSource = originalSource;
   }

   public Object getOriginalSource() {
      return this.originalSource;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return null;
   }

   public int getSize() {
      return 0;
   }

   public String toString() {
      return "address instruction for " + this.originalSource;
   }
}
