package soot.xml;

public class ColorAttribute {
   private int red;
   private int green;
   private int blue;
   private int fg;
   private final String analysisType;

   public ColorAttribute(int red, int green, int blue, boolean fg, String type) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      if (fg) {
         this.fg = 1;
      } else {
         this.fg = 0;
      }

      this.analysisType = type;
   }

   public int red() {
      return this.red;
   }

   public int green() {
      return this.green;
   }

   public int blue() {
      return this.blue;
   }

   public int fg() {
      return this.fg;
   }

   public String analysisType() {
      return this.analysisType;
   }
}
