package soot.coffi;

class Instruction_Dreturn extends Instruction_noargs {
   public Instruction_Dreturn() {
      super((byte)-81);
      this.name = "dreturn";
      this.branches = true;
      this.returns = true;
   }
}
