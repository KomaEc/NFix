package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.tagkit.Tag;

public abstract class TaggedInstruction extends DexlibAbstractInstruction {
   private Tag instructionTag = null;

   public TaggedInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public void setTag(Tag t) {
      this.instructionTag = t;
   }

   public Tag getTag() {
      if (this.instructionTag == null) {
         throw new RuntimeException("Must tag instruction first! (0x" + Integer.toHexString(this.codeAddress) + ": " + this.instruction + ")");
      } else {
         return this.instructionTag;
      }
   }
}
