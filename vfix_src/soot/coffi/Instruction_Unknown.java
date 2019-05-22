package soot.coffi;

class Instruction_Unknown extends Instruction_noargs {
   public Instruction_Unknown(byte c) {
      super(c);
      this.name = "unknown instruction (" + (c & 255) + ")";
   }
}
