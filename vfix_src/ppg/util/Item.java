package ppg.util;

import java.io.IOException;
import java.io.Writer;

abstract class Item {
   Item next = null;

   protected Item() {
   }

   abstract int formatN(int var1, int var2, int var3, int var4, boolean var5, boolean var6) throws Overrun;

   abstract int sendOutput(Writer var1, int var2, int var3) throws IOException;

   void free() {
      if (this.next != null) {
         this.next.free();
         this.next = null;
      }

   }

   static int format(Item it, int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
      if (!nofail && pos > rmargin) {
         throw new Overrun(pos - rmargin);
      } else if (it == null) {
         if (!nofail && pos > fin) {
            throw new Overrun(pos - fin);
         } else {
            return pos;
         }
      } else {
         return it.formatN(lmargin, pos, rmargin, fin, can_break, nofail);
      }
   }
}
