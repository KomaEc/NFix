package soot.jimple.parser.node;

public abstract class Token extends Node {
   private String text;
   private int line;
   private int pos;

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public int getLine() {
      return this.line;
   }

   public void setLine(int line) {
      this.line = line;
   }

   public int getPos() {
      return this.pos;
   }

   public void setPos(int pos) {
      this.pos = pos;
   }

   public String toString() {
      return this.text + " ";
   }

   void removeChild(Node child) {
      throw new RuntimeException("Not a child.");
   }

   void replaceChild(Node oldChild, Node newChild) {
      throw new RuntimeException("Not a child.");
   }
}
