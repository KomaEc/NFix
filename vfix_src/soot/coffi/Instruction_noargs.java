package soot.coffi;

class Instruction_noargs extends Instruction {
   public Instruction_noargs(byte c) {
      super(c);
   }

   public int parse(byte[] bc, int index) {
      return index;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      return index;
   }
}
