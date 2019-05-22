package ppg.util;

import java.io.IOException;
import java.io.Writer;

class StringItem extends Item {
   String s;

   StringItem(String s_) {
      this.s = s_;
   }

   int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
      return format(this.next, lmargin, pos + this.s.length(), rmargin, fin, can_break, nofail);
   }

   int sendOutput(Writer o, int lm, int pos) throws IOException {
      o.write(this.s);
      return pos + this.s.length();
   }

   void appendString(String s) {
      this.s = this.s + s;
   }
}
