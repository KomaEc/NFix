package soot.coffi;

class Instruction_Astore extends Instruction_bytevar implements Interface_Astore {
   public Instruction_Astore() {
      super((byte)58);
      this.name = "astore";
   }

   public int getLocalNumber() {
      return this.arg_b;
   }
}
