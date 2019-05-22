package soot.coffi;

class Instruction_Goto extends Instruction_intbranch {
   public Instruction_Goto() {
      super((byte)-89);
      this.name = "goto";
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{this.target};
      return i;
   }
}
