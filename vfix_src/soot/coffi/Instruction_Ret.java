package soot.coffi;

class Instruction_Ret extends Instruction_bytevar {
   public Instruction_Ret() {
      super((byte)-87);
      this.name = "ret";
      this.branches = true;
   }
}
