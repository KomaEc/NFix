package polyglot.visit;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;

public class StringPrettyPrinter extends PrettyPrinter {
   int maxdepth;
   int depth;

   public StringPrettyPrinter(int maxdepth) {
      this.maxdepth = maxdepth;
      this.depth = 0;
   }

   public void print(Node parent, Node child, CodeWriter w) {
      ++this.depth;
      if (this.depth < this.maxdepth) {
         super.print(parent, child, w);
      } else {
         w.write("...");
      }

      --this.depth;
   }

   public String toString(Node ast) {
      StringPrettyPrinter.StringCodeWriter w = new StringPrettyPrinter.StringCodeWriter(new CharArrayWriter());
      this.print((Node)null, ast, w);

      try {
         w.flush();
      } catch (IOException var4) {
      }

      return w.toString();
   }

   public static class StringCodeWriter extends CodeWriter {
      CharArrayWriter w;

      public StringCodeWriter(CharArrayWriter w) {
         super((Writer)w, 1000);
         this.w = w;
      }

      public void write(String s) {
         StringBuffer sb = new StringBuffer();
         char last = 0;

         for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (!Character.isSpaceChar(c) || !Character.isSpaceChar(last)) {
               sb.append(c);
               last = c;
            }
         }

         super.write(sb.toString());
      }

      public void newline(int n) {
         super.write(" ");
      }

      public void allowBreak(int n) {
         super.write(" ");
      }

      public void allowBreak(int n, String alt) {
         super.write(alt);
      }

      public void begin(int n) {
         super.begin(0);
      }

      public String toString() {
         return this.w.toString();
      }
   }
}
