package soot.JastAddJ;

public class CONSTANT_Methodref_Info extends CONSTANT_Info {
   public int class_index;
   public int name_and_type_index;

   public CONSTANT_Methodref_Info(BytecodeParser parser) {
      super(parser);
      this.class_index = this.p.u2();
      this.name_and_type_index = this.p.u2();
   }

   public String toString() {
      return "MethodRefInfo: " + this.class_index + " " + this.name_and_type_index;
   }
}
