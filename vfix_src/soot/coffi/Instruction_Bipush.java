package soot.coffi;

class Instruction_Bipush extends Instruction_byte {
   public Instruction_Bipush() {
      super((byte)16);
      this.name = "bipush";
   }

   public Instruction_Bipush(byte b) {
      this();
      this.arg_b = b;
   }
}
