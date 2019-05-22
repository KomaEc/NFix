package soot.coffi;

class Instruction_Astore_2 extends Instruction_noargs implements Interface_Astore {
   public Instruction_Astore_2() {
      super((byte)77);
      this.name = "astore_2";
   }

   public int getLocalNumber() {
      return 2;
   }
}
