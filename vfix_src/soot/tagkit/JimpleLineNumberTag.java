package soot.tagkit;

public class JimpleLineNumberTag implements Tag {
   private final int startLineNumber;
   private final int endLineNumber;

   public JimpleLineNumberTag(int ln) {
      this.startLineNumber = ln;
      this.endLineNumber = ln;
   }

   public JimpleLineNumberTag(int startLn, int endLn) {
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

   public String getName() {
      return "JimpleLineNumberTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[2];
      return v;
   }

   public String toString() {
      return "Jimple Line Tag: " + this.startLineNumber;
   }
}
