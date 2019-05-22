package soot.coffi;

class Instruction_Iload extends Instruction_bytevar {
   public Instruction_Iload() {
      super((byte)21);
      this.name = "iload";
   }
}
