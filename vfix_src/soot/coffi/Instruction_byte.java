package soot.coffi;

class Instruction_byte extends Instruction {
   public byte arg_b;

   public Instruction_byte(byte c) {
      super(c);
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + this.arg_b;
   }

   public int nextOffset(int curr) {
      return curr + 2;
   }

   public int parse(byte[] bc, int index) {
      this.arg_b = bc[index];
      return index + 1;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      bc[index++] = this.arg_b;
      return index;
   }

   public String toString() {
      return super.toString() + "    " + this.arg_b;
   }
}
