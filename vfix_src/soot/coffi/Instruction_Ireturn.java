package soot.coffi;

class Instruction_Ireturn extends Instruction_noargs {
   public Instruction_Ireturn() {
      super((byte)-84);
      this.name = "ireturn";
      this.branches = true;
      this.returns = true;
   }
}
