package soot.coffi;

abstract class Instruction implements Cloneable {
   public static final String argsep = " ";
   public static final String LOCALPREFIX = "local_";
   public byte code;
   public int label;
   public String name;
   public Instruction next;
   public Instruction prev;
   public boolean labelled;
   public boolean branches;
   public boolean calls;
   public boolean returns;
   public Instruction[] succs;
   int originalIndex;

   public Instruction(byte c) {
      this.code = c;
      this.next = null;
      this.branches = false;
      this.calls = false;
      this.returns = false;
   }

   protected Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public String toString() {
      return this.label + ": " + this.name + "[" + this.originalIndex + "]";
   }

   public abstract int parse(byte[] var1, int var2);

   public abstract int compile(byte[] var1, int var2);

   public void offsetToPointer(ByteCode bc) {
   }

   public int nextOffset(int curr) {
      return curr + 1;
   }

   public Instruction[] branchpoints(Instruction next) {
      return null;
   }

   public void markCPRefs(boolean[] refs) {
   }

   public void redirectCPRefs(short[] redirect) {
   }

   public int hashCode() {
      return (new Integer(this.label)).hashCode();
   }

   public boolean equals(Instruction i) {
      return this == i;
   }

   public static short getShort(byte[] bc, int index) {
      short bh = (short)bc[index];
      short bl = (short)bc[index + 1];
      short s = (short)(bh << 8 & '\uff00' | bl & 255);
      return s;
   }

   public static int getInt(byte[] bc, int index) {
      int bhh = bc[index] << 24 & -16777216;
      int bhl = bc[index + 1] << 16 & 16711680;
      int blh = bc[index + 2] << 8 & '\uff00';
      int bll = bc[index + 3] & 255;
      int i = bhh | bhl | blh | bll;
      return i;
   }

   public static int shortToBytes(short s, byte[] bc, int index) {
      bc[index++] = (byte)(s >> 8 & 255);
      bc[index++] = (byte)(s & 255);
      return index;
   }

   public static int intToBytes(int s, byte[] bc, int index) {
      bc[index++] = (byte)(s >> 24 & 255);
      bc[index++] = (byte)(s >> 16 & 255);
      bc[index++] = (byte)(s >> 8 & 255);
      bc[index++] = (byte)(s & 255);
      return index;
   }

   public String toString(cp_info[] constant_pool) {
      int i = this.code & 255;
      if (this.name == null) {
         this.name = "null???=" + Integer.toString(i);
      }

      return this.name;
   }
}
