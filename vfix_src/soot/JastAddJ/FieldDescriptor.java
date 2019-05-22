package soot.JastAddJ;

public class FieldDescriptor {
   private BytecodeParser p;
   String typeDescriptor;

   public FieldDescriptor(BytecodeParser parser, String name) {
      this.p = parser;
      int descriptor_index = this.p.u2();
      this.typeDescriptor = ((CONSTANT_Utf8_Info)this.p.constantPool[descriptor_index]).string();
   }

   public Access type() {
      return (new TypeDescriptor(this.p, this.typeDescriptor)).type();
   }

   public boolean isBoolean() {
      return (new TypeDescriptor(this.p, this.typeDescriptor)).isBoolean();
   }
}
