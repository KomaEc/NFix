package soot.xml;

public class PosColorAttribute {
   private ColorAttribute color;
   private int jimpleStartPos;
   private int jimpleEndPos;
   private int javaStartPos;
   private int javaEndPos;
   private int javaStartLn;
   private int javaEndLn;
   private int jimpleStartLn;
   private int jimpleEndLn;

   public ColorAttribute color() {
      return this.color;
   }

   public void color(ColorAttribute c) {
      this.color = c;
   }

   public int jimpleStartPos() {
      return this.jimpleStartPos;
   }

   public void jimpleStartPos(int x) {
      this.jimpleStartPos = x;
   }

   public int jimpleEndPos() {
      return this.jimpleEndPos;
   }

   public void jimpleEndPos(int x) {
      this.jimpleEndPos = x;
   }

   public int javaStartPos() {
      return this.javaStartPos;
   }

   public void javaStartPos(int x) {
      this.javaStartPos = x;
   }

   public int javaEndPos() {
      return this.javaEndPos;
   }

   public void javaEndPos(int x) {
      this.javaEndPos = x;
   }

   public int jimpleStartLn() {
      return this.jimpleStartLn;
   }

   public void jimpleStartLn(int x) {
      this.jimpleStartLn = x;
   }

   public int jimpleEndLn() {
      return this.jimpleEndLn;
   }

   public void jimpleEndLn(int x) {
      this.jimpleEndLn = x;
   }

   public int javaStartLn() {
      return this.javaStartLn;
   }

   public void javaStartLn(int x) {
      this.javaStartLn = x;
   }

   public int javaEndLn() {
      return this.javaEndLn;
   }

   public void javaEndLn(int x) {
      this.javaEndLn = x;
   }

   public boolean hasColor() {
      return this.color() != null;
   }
}
