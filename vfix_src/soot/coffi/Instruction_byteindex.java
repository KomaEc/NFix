package soot.coffi;

class Instruction_byteindex extends Instruction {
   public short arg_b;

   public Instruction_byteindex(byte c) {
      super(c);
   }

   public String toString(cp_info[] constant_pool) {
      int i = this.arg_b & 255;
      return super.toString(constant_pool) + " " + "[" + constant_pool[i].toString(constant_pool) + "]";
   }

   public int nextOffset(int curr) {
      return curr + 2;
   }

   public void markCPRefs(boolean[] refs) {
      refs[this.arg_b & 255] = true;
   }

   public void redirectCPRefs(short[] redirect) {
      this.arg_b = (short)((byte)redirect[this.arg_b & 255]);
   }

   public int parse(byte[] bc, int index) {
      this.arg_b = (short)bc[index];
      this.arg_b = this.arg_b >= 0 ? this.arg_b : (short)(256 + this.arg_b);
      return index + 1;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      bc[index++] = (byte)this.arg_b;
      return index;
   }
}
