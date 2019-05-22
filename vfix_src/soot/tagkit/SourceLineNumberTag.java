package soot.tagkit;

public class SourceLineNumberTag implements Tag {
   private int startLineNumber;
   private int endLineNumber;

   public SourceLineNumberTag(int ln) {
      this.startLineNumber = ln;
      this.endLineNumber = ln;
   }

   public SourceLineNumberTag(int startLn, int endLn) {
      this.startLineNumber = startLn;
      this.endLineNumber = endLn;
   }

   public int getLineNumber() {
      return this.startLineNumber;
   }

   public int getStartLineNumber() {
      return this.startLineNumber;
   }

   public int getEndLineNumber() {
      return this.endLineNumber;
   }

   public void setLineNumber(int value) {
      this.startLineNumber = value;
      this.endLineNumber = value;
   }

   public void setStartLineNumber(int value) {
      this.startLineNumber = value;
   }

   public void setEndLineNumber(int value) {
      this.endLineNumber = value;
   }

   public String getName() {
      return "SourceLineNumberTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[2];
      return v;
   }

   public String toString() {
      return String.valueOf(this.startLineNumber);
   }
}
