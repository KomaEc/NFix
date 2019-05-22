package soot.coffi;

class Instruction_Lload extends Instruction_bytevar {
   public Instruction_Lload() {
      super((byte)22);
      this.name = "lload";
   }
}
