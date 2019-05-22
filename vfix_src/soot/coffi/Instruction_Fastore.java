package soot.coffi;

class Instruction_Fastore extends Instruction_noargs {
   public Instruction_Fastore() {
      super((byte)81);
      this.name = "fastore";
   }
}
