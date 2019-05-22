package soot.coffi;

class Instruction_Aload extends Instruction_bytevar {
   public Instruction_Aload() {
      super((byte)25);
      this.name = "aload";
   }
}
