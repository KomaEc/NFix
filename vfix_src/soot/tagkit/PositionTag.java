package soot.tagkit;

public class PositionTag implements Tag {
   private final int endOffset;
   private final int startOffset;

   public PositionTag(int start, int end) {
      this.startOffset = start;
      this.endOffset = end;
   }

   public int getEndOffset() {
      return this.endOffset;
   }

   public int getStartOffset() {
      return this.startOffset;
   }

   public String getName() {
      return "PositionTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[2];
      return v;
   }

   public String toString() {
      return "Jimple pos tag: spos: " + this.startOffset + " epos: " + this.endOffset;
   }
}
