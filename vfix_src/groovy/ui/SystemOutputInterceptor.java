package groovy.ui;

import groovy.lang.Closure;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SystemOutputInterceptor extends FilterOutputStream {
   private Closure callback;
   private boolean output;

   public SystemOutputInterceptor(Closure callback) {
      this(callback, true);
   }

   public SystemOutputInterceptor(Closure callback, boolean output) {
      super(output ? System.out : System.err);

      assert callback != null;

      this.callback = callback;
      this.output = output;
   }

   public void start() {
      if (this.output) {
         System.setOut(new PrintStream(this));
      } else {
         System.setErr(new PrintStream(this));
      }

   }

   public void stop() {
      if (this.output) {
         System.setOut((PrintStream)this.out);
      } else {
         System.setErr((PrintStream)this.out);
      }

   }

   public void write(byte[] b, int off, int len) throws IOException {
      Boolean result = (Boolean)this.callback.call((Object)(new String(b, off, len)));
      if (result) {
         this.out.write(b, off, len);
      }

   }

   public void write(int b) throws IOException {
      Boolean result = (Boolean)this.callback.call((Object)String.valueOf((char)b));
      if (result) {
         this.out.write(b);
      }

   }
}
