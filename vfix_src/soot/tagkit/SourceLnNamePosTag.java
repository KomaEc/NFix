package soot.tagkit;

public class SourceLnNamePosTag extends SourceLnPosTag {
   protected final String fileName;

   public SourceLnNamePosTag(String fileName, int sline, int eline, int spos, int epos) {
      super(sline, eline, spos, epos);
      this.fileName = fileName;
   }

   public String getFileName() {
      return this.fileName;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append(super.toString());
      sb.append(" file: ");
      sb.append(this.fileName);
      return sb.toString();
   }
}
