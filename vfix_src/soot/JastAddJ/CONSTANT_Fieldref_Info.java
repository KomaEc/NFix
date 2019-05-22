package soot.JastAddJ;

public class CONSTANT_Fieldref_Info extends CONSTANT_Info {
   public int class_index;
   public int name_and_type_index;

   public CONSTANT_Fieldref_Info(BytecodeParser parser) {
      super(parser);
      this.class_index = this.p.u2();
      this.name_and_type_index = this.p.u2();
   }

   public String toString() {
      return "FieldRefInfo: " + this.p.constantPool[this.class_index] + " " + this.p.constantPool[this.name_and_type_index];
   }
}
