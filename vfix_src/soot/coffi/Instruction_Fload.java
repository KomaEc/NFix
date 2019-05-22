package soot.coffi;

class Instruction_Fload extends Instruction_bytevar {
   public Instruction_Fload() {
      super((byte)23);
      this.name = "fload";
   }
}
