package soot.coffi;

class Instruction_Invokeinterface extends Instruction_intindex {
   public byte nargs;
   public byte reserved;

   public Instruction_Invokeinterface() {
      super((byte)-71);
      this.name = "invokeinterface";
      this.calls = true;
   }

   public String toString(cp_info[] constant_pool) {
      return super.toString(constant_pool) + " " + this.nargs + " " + "(reserved " + this.reserved + ")";
   }

   public int nextOffset(int curr) {
      return super.nextOffset(curr) + 2;
   }

   public int parse(byte[] bc, int index) {
      index = super.parse(bc, index);
      this.nargs = bc[index];
      ++index;
      this.reserved = bc[index];
      return index + 1;
   }

   public int compile(byte[] bc, int index) {
      index = super.compile(bc, index);
      bc[index++] = this.nargs;
      bc[index++] = this.reserved;
      return index;
   }
}
