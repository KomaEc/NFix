package soot.tagkit;

public class BytecodeOffsetTag implements Tag {
   private int offset;

   public BytecodeOffsetTag(int offset) {
      this.offset = offset;
   }

   public String getName() {
      return "BytecodeOffsetTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[]{(byte)((this.offset >> 24) % 256), (byte)((this.offset >> 16) % 256), (byte)((this.offset >> 8) % 256), (byte)(this.offset % 256)};
      return v;
   }

   public int getBytecodeOffset() {
      return this.offset;
   }

   public String toString() {
      return "" + this.offset;
   }
}
