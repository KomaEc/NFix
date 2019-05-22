package soot.jimple.toolkits.annotation.tags;

public class ArrayCheckTag implements OneByteCodeTag {
   private static final String NAME = "ArrayCheckTag";
   private boolean lowerCheck = true;
   private boolean upperCheck = true;

   public ArrayCheckTag(boolean lower, boolean upper) {
      this.lowerCheck = lower;
      this.upperCheck = upper;
   }

   public byte[] getValue() {
      byte[] value = new byte[]{0};
      if (this.lowerCheck) {
         value[0] = (byte)(value[0] | 1);
      }

      if (this.upperCheck) {
         value[0] = (byte)(value[0] | 2);
      }

      return value;
   }

   public boolean isCheckUpper() {
      return this.upperCheck;
   }

   public boolean isCheckLower() {
      return this.lowerCheck;
   }

   public String getName() {
      return "ArrayCheckTag";
   }

   public String toString() {
      return (this.lowerCheck ? "[potentially unsafe lower bound]" : "[safe lower bound]") + "" + (this.upperCheck ? "[potentially unsafe upper bound]" : "[safe upper bound]");
   }
}
