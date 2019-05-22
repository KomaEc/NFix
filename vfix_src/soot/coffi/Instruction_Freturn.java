package soot.coffi;

class Instruction_Freturn extends Instruction_noargs {
   public Instruction_Freturn() {
      super((byte)-82);
      this.name = "freturn";
      this.branches = true;
      this.returns = true;
   }
}
