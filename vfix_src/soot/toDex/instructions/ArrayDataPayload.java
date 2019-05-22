package soot.toDex.instructions;

import java.util.List;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderArrayPayload;
import soot.toDex.LabelAssigner;

public class ArrayDataPayload extends AbstractPayload {
   private final int elementWidth;
   private final List<Number> arrayElements;

   public ArrayDataPayload(int elementWidth, List<Number> arrayElements) {
      this.elementWidth = elementWidth;
      this.arrayElements = arrayElements;
   }

   public int getSize() {
      return 4 + (this.arrayElements.size() * this.elementWidth + 1) / 2;
   }

   public int getMaxJumpOffset() {
      return 32767;
   }

   protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
      return new BuilderArrayPayload(this.elementWidth, this.arrayElements);
   }
}
