package soot.coffi;

class Instruction_intvar extends Instruction implements Interface_OneIntArg {
   public int arg_i;

   public Instruction_intvar(byte c) {
      super(c);
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + "local_" + this.arg_i;
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

   public int getIntArg() {
      return this.arg_i;
   }
}
