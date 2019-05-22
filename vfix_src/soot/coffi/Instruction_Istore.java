package soot.coffi;

class Instruction_Istore extends Instruction_bytevar {
   public Instruction_Istore() {
      super((byte)54);
      this.name = "istore";
   }
}
