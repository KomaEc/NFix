package soot.coffi;

class Instruction_If_icmpne extends Instruction_intbranch {
   public Instruction_If_icmpne() {
      super((byte)-96);
      this.name = "if_icmpne";
   }
}
