package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class BlockItem extends Item {
   BlockItem parent;
   Item first;
   Item last;
   int indent;
   Map containsBreaks = new HashMap();

   BlockItem(BlockItem parent_, int indent_) {
      this.parent = parent_;
      this.first = this.last = null;
      this.indent = indent_;
   }

   void add(Item it) {
      if (this.first == null) {
         this.first = it;
      } else {
         if (it instanceof TextItem && this.last instanceof TextItem) {
            TextItem lasts = (TextItem)this.last;
            lasts.appendTextItem((TextItem)it);
            return;
         }

         this.last.next = it;
      }

      this.last = it;
   }

   FormatResult formatN(int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
      int childfin = fin;
      if (fin + getMinPosWidth(this.next, m) > rmargin) {
         childfin = rmargin - getMinPosWidth(this.next, m);
      }

      while(true) {
         FormatResult fr = format(this.first, pos + this.indent, pos, rmargin, childfin, new MaxLevels(m.maxLevelInner, m.maxLevelInner), 0, 0);
         int minLevel2 = Math.max(minLevel, fr.minLevel);
         int minLevelU2 = Math.max(minLevelUnified, fr.minLevel);

         try {
            return format(this.next, lmargin, fr.pos, rmargin, fin, m, minLevel2, minLevelU2);
         } catch (Overrun var13) {
            if (var13.type == 1) {
               var13.type = 2;
               throw var13;
            }

            childfin -= var13.amount;
         }
      }
   }

   int sendOutput(PrintWriter o, int lmargin, int pos, boolean success, Item last) throws IOException {
      Item it = this.first;

      for(lmargin = pos + this.indent; it != null; it = it.next) {
         pos = it.sendOutput(o, lmargin, pos, success, last);
         if (last != null && it == last) {
            throw new IOException();
         }
      }

      return pos;
   }

   int selfMinWidth(MaxLevels m) {
      return getMinWidth(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
   }

   int selfMinPosWidth(MaxLevels m) {
      return getMinPosWidth(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
   }

   int selfMinIndent(MaxLevels m) {
      return getMinIndent(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
   }

   boolean selfContainsBreaks(MaxLevels m) {
      if (this.containsBreaks.containsKey(m)) {
         return this.containsBreaks.get(m) != null;
      } else {
         boolean result = containsBreaks(this.first, new MaxLevels(m.maxLevelInner, m.maxLevelInner));
         this.containsBreaks.put(m, result ? m : null);
         return result;
      }
   }

   String selfToString() {
      return this.indent == 0 ? "[" + this.first + "]" : "[" + this.indent + this.first + "]";
   }
}
