package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;

public class EscapeUnicode extends BaseParamFilterReader implements ChainableReader {
   private StringBuffer unicodeBuf = new StringBuffer();

   public EscapeUnicode() {
   }

   public EscapeUnicode(Reader in) {
      super(in);
   }

   public final int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch = true;
      int ch;
      if (this.unicodeBuf.length() == 0) {
         ch = this.in.read();
         if (ch != -1) {
            char achar = (char)ch;
            if (achar >= 128) {
               this.unicodeBuf = new StringBuffer("u0000");
               String s = Integer.toHexString(ch);

               for(int i = 0; i < s.length(); ++i) {
                  this.unicodeBuf.setCharAt(this.unicodeBuf.length() - s.length() + i, s.charAt(i));
               }

               ch = 92;
            }
         }
      } else {
         ch = this.unicodeBuf.charAt(0);
         this.unicodeBuf.deleteCharAt(0);
      }

      return ch;
   }

   public final Reader chain(Reader rdr) {
      EscapeUnicode newFilter = new EscapeUnicode(rdr);
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
   }
}
