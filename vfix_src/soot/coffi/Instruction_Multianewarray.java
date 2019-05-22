package soot.coffi;

class Instruction_Multianewarray extends Instruction_intindex {
   public byte dims;

   public Instruction_Multianewarray() {
      super((byte)-59);
      this.name = "multianewarray";
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + this.dims;
   }

   public int nextOffset(int curr) {
      return super.nextOffset(curr) + 1;
   }

   public int parse(byte[] bc, int index) {
      index = super.parse(bc, index);
      this.dims = bc[index];
      return index + 1;
   }

   public int compile(byte[] bc, int index) {
      index = super.compile(bc, index);
      bc[index] = this.dims;
      return index + 1;
   }
}
