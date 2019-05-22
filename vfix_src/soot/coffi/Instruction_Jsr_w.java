package soot.coffi;

class Instruction_Jsr_w extends Instruction_longbranch {
   public Instruction_Jsr_w() {
      super((byte)-55);
      this.name = "jsr_w";
   }

   public Instruction[] branchpoints(Instruction next) {
      Instruction[] i = new Instruction[]{this.target};
      return i;
   }
}
