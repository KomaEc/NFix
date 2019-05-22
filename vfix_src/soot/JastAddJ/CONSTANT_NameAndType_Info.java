package soot.JastAddJ;

public class CONSTANT_NameAndType_Info extends CONSTANT_Info {
   public int name_index;
   public int descriptor_index;

   public CONSTANT_NameAndType_Info(BytecodeParser parser) {
      super(parser);
      this.name_index = this.p.u2();
      this.descriptor_index = this.p.u2();
   }

   public String toString() {
      return "NameAndTypeInfo: " + this.name_index + " " + this.descriptor_index;
   }
}
