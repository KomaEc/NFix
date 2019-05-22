package soot.coffi;

class Instruction_Ifnull extends Instruction_intbranch {
   public Instruction_Ifnull() {
      super((byte)-58);
      this.name = "ifnull";
   }
}
