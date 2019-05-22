package soot.tagkit;

public class StringTag implements Tag {
   String s;
   private String analysisType;

   public StringTag(String s, String type) {
      this(s);
      this.analysisType = type;
   }

   public StringTag(String s) {
      this.analysisType = "Unknown";
      this.s = s;
   }

   public String toString() {
      return this.s;
   }

   public String getAnalysisType() {
      return this.analysisType;
   }

   public String getName() {
      return "StringTag";
   }

   public String getInfo() {
      return this.s;
   }

   public byte[] getValue() {
      throw new RuntimeException("StringTag has no value for bytecode");
   }
}
