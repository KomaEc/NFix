package soot.tagkit;

public class KeyTag implements Tag {
   private int red;
   private int green;
   private int blue;
   private String key;
   private String analysisType;

   public KeyTag(int r, int g, int b, String k, String type) {
      this.red = r;
      this.green = g;
      this.blue = b;
      this.key = k;
      this.analysisType = type;
   }

   public KeyTag(int color, String k, String type) {
      this(color, k);
      this.analysisType = type;
   }

   public KeyTag(int color, String k) {
      switch(color) {
      case 0:
         this.red = 255;
         this.green = 0;
         this.blue = 0;
         break;
      case 1:
         this.red = 45;
         this.green = 255;
         this.blue = 84;
         break;
      case 2:
         this.red = 255;
         this.green = 248;
         this.blue = 35;
         break;
      case 3:
         this.red = 174;
         this.green = 210;
         this.blue = 255;
         break;
      case 4:
         this.red = 255;
         this.green = 163;
         this.blue = 0;
         break;
      case 5:
         this.red = 159;
         this.green = 34;
         this.blue = 193;
         break;
      default:
         this.red = 220;
         this.green = 220;
         this.blue = 220;
      }

      this.key = k;
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

   public String key() {
      return this.key;
   }

   public String analysisType() {
      return this.analysisType;
   }

   public String getName() {
      return "KeyTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[4];
      return v;
   }
}
