package soot.coffi;

class Instruction_Sipush extends Instruction_int {
   public Instruction_Sipush() {
      super((byte)17);
      this.name = "sipush";
   }

   public Instruction_Sipush(int i) {
      this();
      this.arg_i = i;
   }
}
