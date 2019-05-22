package ppg.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CodeWriter {
   Block input;
   Block current;
   Writer output;
   int width;

   public CodeWriter(OutputStream o, int width_) {
      this.output = new OutputStreamWriter(o);
      this.width = width_;
      this.current = this.input = new Block((Block)null, 0);
   }

   public CodeWriter(Writer w, int width_) {
      this.output = w;
      this.width = width_;
      this.current = this.input = new Block((Block)null, 0);
   }

   public void write(String s) {
      if (s.length() > 0) {
         this.current.add(new StringItem(s));
      }

   }

   public void newline() {
      this.newline(0);
   }

   public void begin(int n) {
      Block b = new Block(this.current, n);
      this.current.add(b);
      this.current = b;
   }

   public void end() {
      this.current = this.current.parent;
      if (this.current == null) {
         throw new RuntimeException();
      }
   }

   public void allowBreak(int n) {
      this.current.add(new AllowBreak(n, " "));
   }

   public void allowBreak(int n, String alt) {
      this.current.add(new AllowBreak(n, alt));
   }

   public void newline(int n) {
      this.current.add(new Newline(n));
   }

   public boolean flush() throws IOException {
      boolean success = true;

      try {
         Item.format(this.input, 0, 0, this.width, this.width, true, true);
      } catch (Overrun var3) {
         success = false;
      }

      this.input.sendOutput(this.output, 0, 0);
      this.output.flush();
      this.input.free();
      this.current = this.input = new Block((Block)null, 0);
      return success;
   }
}
