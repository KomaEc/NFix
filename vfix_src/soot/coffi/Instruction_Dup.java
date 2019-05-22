package soot.coffi;

class Instruction_Dup extends Instruction_noargs {
   public Instruction_Dup() {
      super((byte)89);
      this.name = "dup";
   }
}
