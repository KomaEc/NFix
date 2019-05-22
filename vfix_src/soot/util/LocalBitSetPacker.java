package soot.util;

import java.util.Iterator;
import soot.Body;
import soot.Local;

public class LocalBitSetPacker {
   private final Body body;
   private Local[] locals;
   private int[] oldNumbers;

   public LocalBitSetPacker(Body body) {
      this.body = body;
   }

   public void pack() {
      int n = this.body.getLocalCount();
      this.locals = new Local[n];
      this.oldNumbers = new int[n];
      n = 0;
      Iterator var2 = this.body.getLocals().iterator();

      while(var2.hasNext()) {
         Local local = (Local)var2.next();
         this.locals[n] = local;
         this.oldNumbers[n] = local.getNumber();
         local.setNumber(n++);
      }

   }

   public void unpack() {
      for(int i = 0; i < this.locals.length; ++i) {
         this.locals[i].setNumber(this.oldNumbers[i]);
      }

      this.locals = null;
      this.oldNumbers = null;
   }

   public int getLocalCount() {
      return this.locals == null ? 0 : this.locals.length;
   }
}
