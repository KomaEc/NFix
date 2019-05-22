package soot.coffi;

class Instruction_Faload extends Instruction_noargs {
   public Instruction_Faload() {
      super((byte)48);
      this.name = "faload";
   }
}
