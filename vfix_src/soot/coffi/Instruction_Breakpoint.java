package soot.coffi;

class Instruction_Breakpoint extends Instruction_noargs {
   public Instruction_Breakpoint() {
      super((byte)-54);
      this.name = "breakpoint";
   }
}
