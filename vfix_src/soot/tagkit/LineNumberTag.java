package soot.tagkit;

public class LineNumberTag implements Tag {
   int line_number;

   public LineNumberTag(int ln) {
      this.line_number = ln;
   }

   public String getName() {
      return "LineNumberTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[]{(byte)(this.line_number / 256), (byte)(this.line_number % 256)};
      return v;
   }

   public int getLineNumber() {
      return this.line_number;
   }

   public void setLineNumber(int value) {
      this.line_number = value;
   }

   public String toString() {
      return String.valueOf(this.line_number);
   }
}
