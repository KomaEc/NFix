package ppg.util;

import java.io.IOException;
import java.io.Writer;

class Newline extends AllowBreak {
   Newline(int n_) {
      super(n_, "");
   }

   int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
      this.broken = true;
      if (!can_break) {
         throw new Overrun(1);
      } else {
         return format(this.next, lmargin, lmargin + this.indent, rmargin, fin, can_break, nofail);
      }
   }

   int sendOutput(Writer o, int lmargin, int pos) throws IOException {
      o.write("\r\n");

      for(int i = 0; i < lmargin + this.indent; ++i) {
         o.write(" ");
      }

      return lmargin + this.indent;
   }
}
