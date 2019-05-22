package soot.xml;

import java.io.PrintWriter;

public class Key {
   private final int red;
   private final int green;
   private final int blue;
   private final String key;
   private String aType;

   public Key(int r, int g, int b, String k) {
      this.red = r;
      this.green = g;
      this.blue = b;
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

   public void print(PrintWriter writerOut) {
      writerOut.println("<key red=\"" + this.red() + "\" green=\"" + this.green() + "\" blue=\"" + this.blue() + "\" key=\"" + this.key() + "\" aType=\"" + this.aType() + "\"/>");
   }

   public String aType() {
      return this.aType;
   }

   public void aType(String s) {
      this.aType = s;
   }
}
