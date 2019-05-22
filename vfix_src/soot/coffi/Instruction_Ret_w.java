package soot.coffi;

class Instruction_Ret_w extends Instruction_intvar {
   public Instruction_Ret_w() {
      super((byte)-47);
      this.name = "ret_w";
      this.branches = true;
   }
}
