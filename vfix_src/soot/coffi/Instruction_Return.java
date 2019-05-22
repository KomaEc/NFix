package soot.coffi;

class Instruction_Return extends Instruction_noargs {
   public Instruction_Return() {
      super((byte)-79);
      this.name = "return";
      this.branches = true;
      this.returns = true;
   }
}
