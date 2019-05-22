package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;

class AllowBreak extends Item {
   final int indent;
   final int level;
   final boolean unified;
   final String alt;
   final int altlen;
   boolean broken = false;

   AllowBreak(int n_, int level_, String alt_, int altlen_, boolean u) {
      this.indent = n_;
      this.alt = alt_;
      this.altlen = altlen_;
      this.level = level_;
      this.unified = u;
   }

   FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
      if (this.canLeaveUnbroken(minLevel, minLevelUnified)) {
         try {
            this.broken = false;
            return format(this.next, lmargin, pos + this.altlen, rmargin, fin, new MaxLevels(Math.min(this.unified ? this.level - 1 : this.level, m.maxLevel), Math.min(this.level - 1, m.maxLevelInner)), minLevel, minLevelUnified);
         } catch (Overrun var10) {
            if (this.level > m.maxLevel) {
               throw var10;
            }
         }
      }

      if (this.canBreak(m)) {
         this.broken = true;

         try {
            return format(this.next, lmargin, lmargin + this.indent, rmargin, fin, m, Math.max(this.level - 1, minLevel), Math.max(this.level, minLevelUnified));
         } catch (Overrun var9) {
            var9.type = 1;
            throw var9;
         }
      } else {
         throw new IllegalArgumentException("could not either break or not break");
      }
   }

   int sendOutput(PrintWriter o, int lmargin, int pos, boolean success, Item last) throws IOException {
      if (!this.broken && success) {
         o.print(this.alt);
         return pos + this.altlen;
      } else {
         o.println();

         for(int i = 0; i < lmargin + this.indent; ++i) {
            o.print(" ");
         }

         return lmargin + this.indent;
      }
   }

   boolean canBreak(MaxLevels m) {
      return this.level <= m.maxLevel;
   }

   boolean canLeaveUnbroken(int minLevel, int minLevelUnified) {
      return this.level > minLevelUnified || !this.unified && this.level > minLevel;
   }

   int selfMinIndent(MaxLevels m) {
      return this.canBreak(m) ? this.indent : -9999;
   }

   int selfMinPosWidth(MaxLevels m) {
      return this.canBreak(m) ? 0 : this.altlen;
   }

   int selfMinWidth(MaxLevels m) {
      return this.canBreak(m) ? this.indent : -9999;
   }

   boolean selfContainsBreaks(MaxLevels m) {
      return this.canBreak(m);
   }

   String selfToString() {
      return this.indent == 0 ? " " : "^" + this.indent;
   }
}
