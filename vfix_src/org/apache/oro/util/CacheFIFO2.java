package org.apache.oro.util;

public final class CacheFIFO2 extends GenericCache {
   private int __current;
   private boolean[] __tryAgain;

   public CacheFIFO2(int var1) {
      super(var1);
      this.__current = 0;
      this.__tryAgain = new boolean[this._cache.length];
   }

   public CacheFIFO2() {
      this(20);
   }

   public synchronized Object getElement(Object var1) {
      Object var2 = this._table.get(var1);
      if (var2 != null) {
         GenericCacheEntry var3 = (GenericCacheEntry)var2;
         this.__tryAgain[var3._index] = true;
         return var3._value;
      } else {
         return null;
      }
   }

   public final synchronized void addElement(Object var1, Object var2) {
      Object var3 = this._table.get(var1);
      if (var3 != null) {
         GenericCacheEntry var4 = (GenericCacheEntry)var3;
         var4._value = var2;
         var4._key = var1;
         this.__tryAgain[var4._index] = true;
      } else {
         int var5;
         if (!this.isFull()) {
            var5 = this._numEntries++;
         } else {
            var5 = this.__current;

            while(this.__tryAgain[var5]) {
               this.__tryAgain[var5] = false;
               ++var5;
               if (var5 >= this.__tryAgain.length) {
                  var5 = 0;
               }
            }

            this.__current = var5 + 1;
            if (this.__current >= this._cache.length) {
               this.__current = 0;
            }

            this._table.remove(this._cache[var5]._key);
         }

         this._cache[var5]._value = var2;
         this._cache[var5]._key = var1;
         this._table.put(var1, this._cache[var5]);
      }
   }
}
