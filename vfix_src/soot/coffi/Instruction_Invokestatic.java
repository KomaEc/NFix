package soot.coffi;

class Instruction_Invokestatic extends Instruction_intindex {
   public Instruction_Invokestatic() {
      super((byte)-72);
      this.name = "invokestatic";
      this.calls = true;
   }
}
