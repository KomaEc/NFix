package soot.coffi;

class Instruction_Areturn extends Instruction_noargs {
   public Instruction_Areturn() {
      super((byte)-80);
      this.name = "areturn";
      this.branches = true;
      this.returns = true;
   }
}
