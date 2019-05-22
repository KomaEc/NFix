package soot.coffi;

class Instruction_New extends Instruction_intindex {
   public Instruction_New() {
      super((byte)-69);
      this.name = "new";
      this.calls = true;
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{null};
      return i;
   }
}
