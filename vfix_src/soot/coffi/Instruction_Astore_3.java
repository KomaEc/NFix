package soot.coffi;

class Instruction_Astore_3 extends Instruction_noargs implements Interface_Astore {
   public Instruction_Astore_3() {
      super((byte)78);
      this.name = "astore_3";
   }

   public int getLocalNumber() {
      return 3;
   }
}
