package soot.coffi;

class Instruction_Lstore extends Instruction_bytevar {
   public Instruction_Lstore() {
      super((byte)55);
      this.name = "lstore";
   }
}
