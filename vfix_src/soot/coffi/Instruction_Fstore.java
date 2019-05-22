package soot.coffi;

class Instruction_Fstore extends Instruction_bytevar {
   public Instruction_Fstore() {
      super((byte)56);
      this.name = "fstore";
   }
}
