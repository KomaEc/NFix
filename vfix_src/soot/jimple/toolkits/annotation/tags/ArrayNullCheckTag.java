package soot.jimple.toolkits.annotation.tags;

public class ArrayNullCheckTag implements OneByteCodeTag {
   private static final String NAME = "ArrayNullCheckTag";
   private byte value = 0;

   public ArrayNullCheckTag() {
   }

   public ArrayNullCheckTag(byte v) {
      this.value = v;
   }

   public String getName() {
      return "ArrayNullCheckTag";
   }

   public byte[] getValue() {
      byte[] bv = new byte[]{this.value};
      return bv;
   }

   public String toString() {
      return Byte.toString(this.value);
   }

   public byte accumulate(byte other) {
      byte oldv = this.value;
      this.value |= other;
      return oldv;
   }
}
