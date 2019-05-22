package soot.coffi;

class Instruction_Lastore extends Instruction_noargs {
   public Instruction_Lastore() {
      super((byte)80);
      this.name = "lastore";
   }
}
