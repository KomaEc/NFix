package soot.coffi;

class Instruction_Sastore extends Instruction_noargs {
   public Instruction_Sastore() {
      super((byte)86);
      this.name = "sastore";
   }
}
