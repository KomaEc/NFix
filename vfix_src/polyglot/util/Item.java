package polyglot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

abstract class Item {
   Item next = null;
   static final int NO_WIDTH = -9999;
   Map min_widths = new HashMap();
   Map min_indents = new HashMap();
   Map min_pos_width = new HashMap();
   boolean contains_brks;
   boolean cb_init = false;

   protected Item() {
   }

   abstract FormatResult formatN(int var1, int var2, int var3, int var4, MaxLevels var5, int var6, int var7) throws Overrun;

   abstract int sendOutput(PrintWriter var1, int var2, int var3, boolean var4, Item var5) throws IOException;

   static FormatResult format(Item it, int lmargin, int pos, int rmargin, int fin, MaxLevels m, int minLevel, int minLevelUnified) throws Overrun {
      ++CodeWriter.format_calls;
      if (it == null) {
         if (pos > fin) {
            throw Overrun.overrun(pos - fin, 2);
         } else {
            return new FormatResult(pos, minLevelUnified);
         }
      } else {
         int amount2 = lmargin + getMinWidth(it, m) - rmargin;
         if (amount2 > 0) {
            throw Overrun.overrun(amount2, 1);
         } else {
            int amount = pos + getMinPosWidth(it, m) - rmargin;
            if (amount > 0) {
               throw Overrun.overrun(amount, 0);
            } else {
               int amount3 = lmargin + getMinIndent(it, m) - fin;
               if (amount3 > 0) {
                  throw Overrun.overrun(amount3, 2);
               } else {
                  return it.formatN(lmargin, pos, rmargin, fin, m, minLevel, minLevelUnified);
               }
            }
         }
      }
   }

   static int getMinWidth(Item it, MaxLevels m) {
      if (it == null) {
         return -9999;
      } else if (it.min_widths.containsKey(m)) {
         return (Integer)it.min_widths.get(m);
      } else {
         int p1 = it.selfMinWidth(m);
         int p2 = it.selfMinIndent(m);
         int p3 = p2 != -9999 ? getMinPosWidth(it.next, m) + p2 : -9999;
         int p4 = getMinWidth(it.next, m);
         int result = Math.max(Math.max(p1, p3), p4);
         it.min_widths.put(m, new Integer(result));
         return result;
      }
   }

   static int getMinPosWidth(Item it, MaxLevels m) {
      if (it == null) {
         return 0;
      } else if (it.min_pos_width.containsKey(m)) {
         return (Integer)it.min_pos_width.get(m);
      } else {
         int p1 = it.selfMinPosWidth(m);
         int result;
         if (it.next != null && !it.selfContainsBreaks(m)) {
            result = p1 + getMinPosWidth(it.next, m);
         } else {
            result = p1;
         }

         it.min_pos_width.put(m, new Integer(result));
         return result;
      }
   }

   static int getMinIndent(Item it, MaxLevels m) {
      if (it == null) {
         return -9999;
      } else if (it.min_indents.containsKey(m)) {
         return (Integer)it.min_indents.get(m);
      } else {
         int p1 = it.selfMinIndent(m);
         if (it.next == null) {
            return p1;
         } else {
            int result;
            if (containsBreaks(it.next, m)) {
               result = getMinIndent(it.next, m);
            } else {
               result = getMinPosWidth(it.next, m);
            }

            it.min_indents.put(m, new Integer(result));
            return result;
         }
      }
   }

   static boolean containsBreaks(Item it, MaxLevels m) {
      if (it == null) {
         return false;
      } else if (it.cb_init) {
         return it.contains_brks;
      } else if (it.selfContainsBreaks(m)) {
         it.contains_brks = true;
         it.cb_init = true;
         return true;
      } else if (it.next == null) {
         return false;
      } else {
         it.contains_brks = containsBreaks(it.next, m);
         it.cb_init = true;
         return it.contains_brks;
      }
   }

   public String summarize(String s) {
      return s.length() <= 79 ? s : s.substring(0, 76) + "...";
   }

   public String toString() {
      return this.next == null ? this.summarize(this.selfToString()) : this.summarize(this.selfToString() + this.next.toString());
   }

   abstract String selfToString();

   abstract int selfMinIndent(MaxLevels var1);

   abstract int selfMinWidth(MaxLevels var1);

   abstract int selfMinPosWidth(MaxLevels var1);

   abstract boolean selfContainsBreaks(MaxLevels var1);
}
