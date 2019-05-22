package soot.coffi;

class Instruction_Iflt extends Instruction_intbranch {
   public Instruction_Iflt() {
      super((byte)-101);
      this.name = "iflt";
   }
}
