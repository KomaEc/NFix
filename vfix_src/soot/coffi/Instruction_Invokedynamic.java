package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction_Invokedynamic extends Instruction_intindex {
   private static final Logger logger = LoggerFactory.getLogger(Instruction_Invokedynamic.class);
   public short invoke_dynamic_index;
   public short reserved;

   public Instruction_Invokedynamic() {
      super((byte)-70);
      this.name = "invokedynamic";
      this.calls = true;
   }

   public int parse(byte[] bc, int index) {
      this.invoke_dynamic_index = getShort(bc, index);
      index += 2;
      this.reserved = getShort(bc, index);
      if (this.reserved > 0) {
         logger.debug("reserved value in invokedynamic is " + this.reserved);
      }

      index += 2;
      return index;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      shortToBytes(this.invoke_dynamic_index, bc, index);
      index += 2;
      shortToBytes(this.reserved, bc, index);
      index += 2;
      return index;
   }
}
