package soot.coffi;

class Instruction_Athrow extends Instruction_noargs {
   public Instruction_Athrow() {
      super((byte)-65);
      this.name = "athrow";
      this.branches = true;
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{null};
      return i;
   }
}
