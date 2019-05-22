package soot.coffi;

class Instruction_Dload extends Instruction_bytevar {
   public Instruction_Dload() {
      super((byte)24);
      this.name = "dload";
   }
}
