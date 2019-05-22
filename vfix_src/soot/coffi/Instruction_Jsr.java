package soot.coffi;

class Instruction_Jsr extends Instruction_intbranch {
   public Instruction_Jsr() {
      super((byte)-88);
      this.name = "jsr";
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{this.target};
      return i;
   }
}
