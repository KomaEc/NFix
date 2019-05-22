package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

class TextItem extends Item {
   String s;
   int length;

   TextItem(String s_, int length_) {
      this.s = s_;
      this.length = length_;
   }

   FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
      return format(this.next, lmargin, pos + this.length, rmargin, fin, m, minLevel, minLevelUnified);
   }

   int sendOutput(PrintWriter o, int lm, int pos, boolean success, Item last) throws IOException {
      o.write(this.s);
      return pos + this.length;
   }

   boolean selfContainsBreaks(MaxLevels m) {
      return false;
   }

   int selfMinIndent(MaxLevels m) {
      return -9999;
   }

   int selfMinWidth(MaxLevels m) {
      return -9999;
   }

   int selfMinPosWidth(MaxLevels m) {
      return this.length;
   }

   String selfToString() {
      StringWriter sw = new StringWriter();

      for(int i = 0; i < this.s.length(); ++i) {
         char c = this.s.charAt(i);
         if (c == ' ') {
            sw.write("\\ ");
         } else {
            sw.write(c);
         }
      }

      return sw.toString();
   }

   public void appendTextItem(TextItem item) {
      this.s = this.s + item.s;
      this.length += item.length;
   }
}
