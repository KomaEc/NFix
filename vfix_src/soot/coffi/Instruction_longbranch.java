package soot.coffi;

class Instruction_longbranch extends Instruction_branch {
   public Instruction_longbranch(byte c) {
      super(c);
   }

   public int nextOffset(int curr) {
      return curr + 5;
   }

   public int parse(byte[] bc, int index) {
      this.arg_i = getInt(bc, index);
      return index + 4;
   }

   public int compile(byte[] bc, int index) {
      bc[index++] = this.code;
      if (this.target != null) {
         shortToBytes((short)(this.target.label - this.label), bc, index);
      } else {
         shortToBytes((short)this.arg_i, bc, index);
      }

      return index + 4;
   }
}
