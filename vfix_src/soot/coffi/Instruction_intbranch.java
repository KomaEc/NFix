package soot.coffi;

class Instruction_intbranch extends Instruction_branch {
   public Instruction_intbranch(byte c) {
      super(c);
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
      if (this.target != null) {
         shortToBytes((short)(this.target.label - this.label), bc, index);
      } else {
         shortToBytes((short)this.arg_i, bc, index);
      }

      return index + 2;
   }
}
