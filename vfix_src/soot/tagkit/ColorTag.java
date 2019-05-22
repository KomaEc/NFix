package soot.tagkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorTag implements Tag {
   private static final Logger logger = LoggerFactory.getLogger(ColorTag.class);
   private int red;
   private int green;
   private int blue;
   private boolean foreground;
   private String analysisType;
   public static final int RED = 0;
   public static final int GREEN = 1;
   public static final int YELLOW = 2;
   public static final int BLUE = 3;
   public static final int ORANGE = 4;
   public static final int PURPLE = 5;

   public ColorTag(int r, int g, int b, boolean fg) {
      this.foreground = false;
      this.analysisType = "Unknown";
      this.red = r;
      this.green = g;
      this.blue = b;
      this.foreground = fg;
   }

   public ColorTag(int r, int g, int b) {
      this(r, g, b, false);
   }

   public ColorTag(int r, int g, int b, String type) {
      this(r, g, b, false, type);
   }

   public ColorTag(int r, int g, int b, boolean fg, String type) {
      this(r, g, b, false);
      this.analysisType = type;
   }

   public ColorTag(int color, String type) {
      this(color, false, type);
   }

   public ColorTag(int color, boolean fg, String type) {
      this(color, fg);
      this.analysisType = type;
   }

   public ColorTag(int color) {
      this(color, false);
   }

   public ColorTag(int color, boolean fg) {
      this.foreground = false;
      this.analysisType = "Unknown";
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

      this.foreground = fg;
   }

   public String getAnalysisType() {
      return this.analysisType;
   }

   public int getRed() {
      return this.red;
   }

   public int getGreen() {
      return this.green;
   }

   public int getBlue() {
      return this.blue;
   }

   public boolean isForeground() {
      return this.foreground;
   }

   public String getName() {
      return "ColorTag";
   }

   public byte[] getValue() {
      byte[] v = new byte[2];
      return v;
   }

   public String toString() {
      return "" + this.red + " " + this.green + " " + this.blue;
   }
}
