package soot.coffi;

class Instruction_Invokevirtual extends Instruction_intindex {
   public Instruction_Invokevirtual() {
      super((byte)-74);
      this.name = "invokevirtual";
      this.calls = true;
   }
}
