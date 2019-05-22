package ppg.util;

import java.io.IOException;
import java.io.Writer;

class AllowBreak extends Item {
   int indent;
   boolean broken = true;
   String alt;

   AllowBreak(int n_, String alt_) {
      this.indent = n_;
      this.alt = alt_;
   }

   int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
      if (can_break) {
         pos = lmargin + this.indent;
         this.broken = true;
      } else {
         pos += this.alt.length();
         this.broken = false;
      }

      return format(this.next, lmargin, pos, rmargin, fin, can_break, nofail);
   }

   int sendOutput(Writer o, int lmargin, int pos) throws IOException {
      if (!this.broken) {
         o.write(this.alt);
         return pos + this.alt.length();
      } else {
         o.write("\r\n");

         for(int i = 0; i < lmargin + this.indent; ++i) {
            o.write(" ");
         }

         return lmargin + this.indent;
      }
   }
}
