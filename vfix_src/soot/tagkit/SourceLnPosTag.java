package soot.tagkit;

public class SourceLnPosTag implements Tag {
   private final int startLn;
   private final int endLn;
   private final int startPos;
   private final int endPos;

   public SourceLnPosTag(int sline, int eline, int spos, int epos) {
      this.startLn = sline;
      this.endLn = eline;
      this.startPos = spos;
      this.endPos = epos;
   }

   public int startLn() {
      return this.startLn;
   }

   public int endLn() {
      return this.endLn;
   }

   public int startPos() {
      return this.startPos;
   }

   public int endPos() {
      return this.endPos;
   }

   public String getName() {
      return "SourceLnPosTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[]{(byte)(this.startLn / 256), (byte)(this.startLn % 256)};
      return v;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("Source Line Pos Tag: ");
      sb.append("sline: ");
      sb.append(this.startLn);
      sb.append(" eline: ");
      sb.append(this.endLn);
      sb.append(" spos: ");
      sb.append(this.startPos);
      sb.append(" epos: ");
      sb.append(this.endPos);
      return sb.toString();
   }
}
