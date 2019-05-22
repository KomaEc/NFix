package groovy.util;

import java.io.PrintWriter;

public class IndentPrinter {
   private int indentLevel;
   private String indent;
   private PrintWriter out;
   private final boolean addNewlines;

   public IndentPrinter() {
      this(new PrintWriter(System.out), "  ");
   }

   public IndentPrinter(PrintWriter out) {
      this(out, "  ");
   }

   public IndentPrinter(PrintWriter out, String indent) {
      this(out, indent, true);
   }

   public IndentPrinter(PrintWriter out, String indent, boolean addNewlines) {
      this.addNewlines = addNewlines;
      if (out == null) {
         out = new PrintWriter(System.out);
      }

      this.out = out;
      this.indent = indent;
   }

   public void println(String text) {
      this.out.print(text);
      this.println();
   }

   public void print(String text) {
      this.out.print(text);
   }

   public void print(char c) {
      this.out.print(c);
   }

   public void printIndent() {
      for(int i = 0; i < this.indentLevel; ++i) {
         this.out.print(this.indent);
      }

   }

   public void println() {
      if (this.addNewlines) {
         this.out.print("\n");
      }

   }

   public void incrementIndent() {
      ++this.indentLevel;
   }

   public void decrementIndent() {
      --this.indentLevel;
   }

   public int getIndentLevel() {
      return this.indentLevel;
   }

   public void setIndentLevel(int indentLevel) {
      this.indentLevel = indentLevel;
   }

   public void flush() {
      this.out.flush();
   }
}
