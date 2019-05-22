package polyglot.lex;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class EscapedUnicodeReader extends FilterReader {
   int pushback = -1;
   boolean isEvenSlash = true;

   public EscapedUnicodeReader(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      int r = this.pushback == -1 ? this.in.read() : this.pushback;
      this.pushback = -1;
      if (r != 92) {
         this.isEvenSlash = true;
         return r;
      } else if (!this.isEvenSlash) {
         this.isEvenSlash = true;
         return r;
      } else {
         this.pushback = this.in.read();
         if (this.pushback != 117) {
            this.isEvenSlash = false;
            return 92;
         } else {
            this.pushback = -1;

            while((r = this.in.read()) == 117) {
            }

            int val = 0;

            for(int i = 0; i < 4; r = this.in.read()) {
               int d = Character.digit((char)r, 16);
               if (r < 0 || d < 0) {
                  throw new Error("Invalid unicode escape character.");
               }

               val = val * 16 + d;
               ++i;
            }

            this.pushback = r;
            this.isEvenSlash = true;
            return val;
         }
      }
   }

   public int read(char[] cbuf, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         int c = this.read();
         if (c == -1) {
            return i == 0 ? -1 : i;
         }

         cbuf[i + off] = (char)c;
      }

      return len;
   }

   public boolean markSupported() {
      return false;
   }

   public boolean ready() throws IOException {
      return this.pushback != -1 ? true : this.in.ready();
   }
}
