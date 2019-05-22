package soot.coffi;

class Instruction_Lreturn extends Instruction_noargs {
   public Instruction_Lreturn() {
      super((byte)-83);
      this.name = "lreturn";
      this.branches = true;
      this.returns = true;
   }
}
