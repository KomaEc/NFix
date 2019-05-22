package soot.coffi;

class Instruction_int extends Instruction {
   public int arg_i;

   public Instruction_int(byte c) {
      super(c);
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + this.arg_i;
   }

   public int nextOffset(int curr) {
      return curr + 3;
   }

   public int parse(byte[] bc, int index) {
      this.arg_i = getShort(bc, index);
      return index + 2;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      shortToBytes((short)this.arg_i, bc, index);
      return index + 2;
   }
}
