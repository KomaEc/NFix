package soot.coffi;

class Instruction_Nop extends Instruction_noargs {
   public Instruction_Nop() {
      super((byte)0);
      this.name = "nop";
   }
}
