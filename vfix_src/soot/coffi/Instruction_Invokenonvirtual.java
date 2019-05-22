package soot.coffi;

class Instruction_Invokenonvirtual extends Instruction_intindex {
   public Instruction_Invokenonvirtual() {
      super((byte)-73);
      this.name = "invokenonvirtual";
      this.calls = true;
   }
}
