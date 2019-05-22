package soot.jimple.toolkits.annotation.tags;

public class NullCheckTag implements OneByteCodeTag {
   private static final String NAME = "NullCheckTag";
   private byte value = 0;

   public NullCheckTag(boolean needCheck) {
      if (needCheck) {
         this.value = 4;
      }

   }

   public String getName() {
      return "NullCheckTag";
   }

   public byte[] getValue() {
      byte[] bv = new byte[]{this.value};
      return bv;
   }

   public boolean needCheck() {
      return this.value != 0;
   }

   public String toString() {
      return this.value == 0 ? "[not null]" : "[unknown]";
   }
}
