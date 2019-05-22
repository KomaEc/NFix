package soot.coffi;

class Instruction_Goto_w extends Instruction_longbranch {
   public Instruction_Goto_w() {
      super((byte)-56);
      this.name = "goto_w";
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{this.target};
      return i;
   }
}
