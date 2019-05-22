package soot.coffi;

class Instruction_Pop extends Instruction_noargs {
   public Instruction_Pop() {
      super((byte)87);
      this.name = "pop";
   }
}
