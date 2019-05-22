package soot.xml;

public class LinkAttribute {
   private String info;
   private int jimpleLink;
   private int javaLink;
   private String className;
   private final boolean isJimpleLink;
   private final boolean isJavaLink;
   private final String analysisType;

   public LinkAttribute(String info, int jimpleLink, int javaLink, String className, String type) {
      this.info = info;
      this.jimpleLink = jimpleLink;
      this.javaLink = javaLink;
      this.className = className;
      this.isJimpleLink = true;
      this.isJavaLink = true;
      this.analysisType = type;
   }

   public String info() {
      return this.info;
   }

   public int jimpleLink() {
      return this.jimpleLink;
   }

   public int javaLink() {
      return this.javaLink;
   }

   public String className() {
      return this.className;
   }

   public boolean isJimpleLink() {
      return this.isJimpleLink;
   }

   public boolean isJavaLink() {
      return this.isJavaLink;
   }

   public String analysisType() {
      return this.analysisType;
   }
}
