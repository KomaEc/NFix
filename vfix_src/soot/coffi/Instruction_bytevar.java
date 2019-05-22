package soot.coffi;

class Instruction_bytevar extends Instruction implements Interface_OneIntArg {
   public int arg_b;
   public boolean isWide;

   public Instruction_bytevar(byte c) {
      super(c);
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + "local_" + this.arg_b;
   }

   public int nextOffset(int curr) {
      return curr + 1 + (this.isWide ? 3 : 1);
   }

   public int parse(byte[] bc, int index) {
      int indexbyte1 = bc[index] & 255;
      if (this.isWide) {
         int indexbyte2 = bc[index + 1] & 255;
         this.arg_b = indexbyte1 << 8 | indexbyte2;
         return index + 2;
      } else {
         this.arg_b = indexbyte1;
         return index + 1;
      }
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      bc[index++] = (byte)this.arg_b;
      return index;
   }

   public int getIntArg() {
      return this.arg_b;
   }
}
