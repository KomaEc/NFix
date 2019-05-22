package soot.coffi;

class Instruction_Iinc extends Instruction_bytevar {
   public int arg_c;

   public Instruction_Iinc() {
      super((byte)-124);
      this.name = "iinc";
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + this.arg_c;
   }

   public int nextOffset(int curr) {
      return super.nextOffset(curr) + (this.isWide ? 2 : 1);
   }

   public int parse(byte[] bc, int index) {
      index = super.parse(bc, index);
      if (!this.isWide) {
         this.arg_c = bc[index];
         return index + 1;
      } else {
         int constbyte1 = bc[index] & 255;
         int constbyte2 = bc[index + 1] & 255;
         this.arg_c = (short)(constbyte1 << 8 | constbyte2);
         return index + 2;
      }
   }

   public int compile(byte[] bc, int index) {
      index = super.compile(bc, index);
      bc[index] = (byte)(this.arg_c & 255);
      return index + 1;
   }
}
