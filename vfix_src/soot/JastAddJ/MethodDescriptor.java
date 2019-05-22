package soot.JastAddJ;

public class MethodDescriptor {
   private BytecodeParser p;
   private String parameterDescriptors;
   private String typeDescriptor;

   public MethodDescriptor(BytecodeParser parser, String name) {
      this.p = parser;
      int descriptor_index = this.p.u2();
      String descriptor = ((CONSTANT_Utf8_Info)this.p.constantPool[descriptor_index]).string();
      int pos = descriptor.indexOf(41);
      this.parameterDescriptors = descriptor.substring(1, pos);
      this.typeDescriptor = descriptor.substring(pos + 1, descriptor.length());
   }

   public List parameterList() {
      TypeDescriptor d = new TypeDescriptor(this.p, this.parameterDescriptors);
      return d.parameterList();
   }

   public List parameterListSkipFirst() {
      TypeDescriptor d = new TypeDescriptor(this.p, this.parameterDescriptors);
      return d.parameterListSkipFirst();
   }

   public Access type() {
      TypeDescriptor d = new TypeDescriptor(this.p, this.typeDescriptor);
      return d.type();
   }
}
