package ppg.util;

import java.io.IOException;
import java.io.Writer;

class Block extends Item {
   Block parent;
   Item first;
   Item last;
   int indent;

   Block(Block parent_, int indent_) {
      this.parent = parent_;
      this.first = this.last = null;
      this.indent = indent_;
   }

   void add(Item it) {
      if (this.first == null) {
         this.first = it;
      } else {
         if (it instanceof StringItem && this.last instanceof StringItem) {
            StringItem lasts = (StringItem)this.last;
            lasts.appendString(((StringItem)it).s);
            return;
         }

         this.last.next = it;
      }

      this.last = it;
   }

   int formatN(int lmargin, int pos, int rmargin, int fin, boolean can_break, boolean nofail) throws Overrun {
      int this_fin = rmargin;
      boolean this_nofail = false;
      boolean this_break = false;

      while(true) {
         int next_pos;
         while(true) {
            try {
               next_pos = format(this.first, pos + this.indent, pos, rmargin, this_fin, this_break, this_nofail && this_break);
               break;
            } catch (Overrun var12) {
               if (!can_break) {
                  throw var12;
               }

               if (!this_break) {
                  this_break = true;
               } else {
                  if (!nofail) {
                     throw var12;
                  }

                  this_nofail = true;
               }
            }
         }

         try {
            return format(this.next, lmargin, next_pos, rmargin, fin, can_break, nofail);
         } catch (Overrun var13) {
            if (!can_break) {
               throw var13;
            }

            if (this.next instanceof AllowBreak) {
               throw var13;
            }

            this_break = true;
            if (next_pos > this_fin) {
               next_pos = this_fin;
            }

            this_fin = next_pos - var13.amount;
         }
      }
   }

   int sendOutput(Writer o, int lmargin, int pos) throws IOException {
      Item it = this.first;

      for(lmargin = pos + this.indent; it != null; it = it.next) {
         pos = it.sendOutput(o, lmargin, pos);
      }

      return pos;
   }

   void free() {
      super.free();
      this.parent = null;
      if (this.first != null) {
         this.first.free();
      }

      this.last = null;
   }
}
