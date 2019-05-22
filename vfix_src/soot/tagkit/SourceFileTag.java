package soot.tagkit;

import java.io.UnsupportedEncodingException;

public class SourceFileTag implements Tag {
   private String sourceFile;
   private String absolutePath;

   public SourceFileTag(String sourceFile) {
      this(sourceFile, (String)null);
   }

   public SourceFileTag(String sourceFile, String path) {
      this.sourceFile = sourceFile.intern();
      this.absolutePath = path;
   }

   public SourceFileTag() {
   }

   public String getName() {
      return "SourceFileTag";
   }

   public byte[] getValue() {
      try {
         return this.sourceFile.getBytes("UTF8");
      } catch (UnsupportedEncodingException var2) {
         return new byte[0];
      }
   }

   public void setSourceFile(String srcFile) {
      this.sourceFile = srcFile.intern();
   }

   public String getSourceFile() {
      return this.sourceFile;
   }

   public void setAbsolutePath(String path) {
      this.absolutePath = path;
   }

   public String getAbsolutePath() {
      return this.absolutePath;
   }

   public String toString() {
      return this.sourceFile;
   }
}
