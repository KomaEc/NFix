package polyglot.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class CodeWriter {
   BlockItem input;
   BlockItem current;
   static Item top;
   PrintWriter output;
   int width;
   static int format_calls = 0;
   public static final boolean debug = false;
   public static final boolean precompute = true;

   public CodeWriter(OutputStream o, int width_) {
      this.output = new PrintWriter(new OutputStreamWriter(o));
      this.width = width_;
      this.current = this.input = new BlockItem((BlockItem)null, 0);
   }

   public CodeWriter(PrintWriter o, int width_) {
      this.output = o;
      this.width = width_;
      this.current = this.input = new BlockItem((BlockItem)null, 0);
   }

   public CodeWriter(Writer o, int width_) {
      this.output = new PrintWriter(o);
      this.width = width_;
      this.current = this.input = new BlockItem((BlockItem)null, 0);
   }

   public void write(String s) {
      if (s.length() > 0) {
         this.write(s, s.length());
      }

   }

   public void write(String s, int length) {
      this.current.add(new TextItem(s, length));
   }

   public void begin(int n) {
      BlockItem b = new BlockItem(this.current, n);
      this.current.add(b);
      this.current = b;
   }

   public void end() {
      this.current = this.current.parent;
   }

   public void allowBreak(int n, int level, String alt, int altlen) {
      this.current.add(new AllowBreak(n, level, alt, altlen, false));
   }

   public void unifiedBreak(int n, int level, String alt, int altlen) {
      this.current.add(new AllowBreak(n, level, alt, altlen, true));
   }

   public void allowBreak(int n) {
      this.allowBreak(n, 1, " ", 1);
   }

   public void allowBreak(int n, String alt) {
      this.allowBreak(n, 1, alt, 1);
   }

   public void newline() {
      this.current.add(new AllowBreak(0, 0, "", 0, false));
   }

   public void newline(int n) {
      this.current.add(new AllowBreak(n, 0, "", 0, false));
   }

   public boolean flush() throws IOException {
      return this.flush(true);
   }

   public boolean flush(boolean format) throws IOException {
      boolean success = true;
      format_calls = 0;
      if (format) {
         try {
            top = this.input;
            Item.format(this.input, 0, 0, this.width, this.width, new MaxLevels(Integer.MAX_VALUE, Integer.MAX_VALUE), 0, 0);
         } catch (Overrun var4) {
            success = false;
         }
      } else {
         success = false;
      }

      this.input.sendOutput(this.output, 0, 0, success, (Item)null);
      this.output.flush();
      this.current = this.input = new BlockItem((BlockItem)null, 0);
      return success;
   }

   public String toString() {
      return this.input.toString();
   }
}
