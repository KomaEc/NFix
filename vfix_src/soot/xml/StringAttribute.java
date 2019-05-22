package soot.xml;

public class StringAttribute {
   private String info;
   private final String analysisType;

   public StringAttribute(String info, String type) {
      this.info = info;
      this.analysisType = type;
   }

   public String info() {
      return this.info;
   }

   public String analysisType() {
      return this.analysisType;
   }
}
