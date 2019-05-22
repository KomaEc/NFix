package soot.coffi;

class Instruction_Astore_1 extends Instruction_noargs implements Interface_Astore {
   public Instruction_Astore_1() {
      super((byte)76);
      this.name = "astore_1";
   }

   public int getLocalNumber() {
      return 1;
   }
}
