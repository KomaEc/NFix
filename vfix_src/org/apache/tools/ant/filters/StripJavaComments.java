package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;

public final class StripJavaComments extends BaseFilterReader implements ChainableReader {
   private int readAheadCh = -1;
   private boolean inString = false;
   private boolean quoted = false;

   public StripJavaComments() {
   }

   public StripJavaComments(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      int ch = true;
      int ch;
      if (this.readAheadCh != -1) {
         ch = this.readAheadCh;
         this.readAheadCh = -1;
      } else {
         ch = this.in.read();
         if (ch == 34 && !this.quoted) {
            this.inString = !this.inString;
            this.quoted = false;
         } else if (ch == 92) {
            this.quoted = !this.quoted;
         } else {
            this.quoted = false;
            if (!this.inString && ch == 47) {
               ch = this.in.read();
               if (ch == 47) {
                  while(ch != 10 && ch != -1 && ch != 13) {
                     ch = this.in.read();
                  }
               } else if (ch != 42) {
                  this.readAheadCh = ch;
                  ch = 47;
               } else {
                  while(true) {
                     do {
                        if (ch == -1) {
                           return ch;
                        }

                        ch = this.in.read();
                     } while(ch != 42);

                     for(ch = this.in.read(); ch == 42 && ch != -1; ch = this.in.read()) {
                     }

                     if (ch == 47) {
                        ch = this.read();
                        break;
                     }
                  }
               }
            }
         }
      }

      return ch;
   }

   public Reader chain(Reader rdr) {
      StripJavaComments newFilter = new StripJavaComments(rdr);
      return newFilter;
   }
}
