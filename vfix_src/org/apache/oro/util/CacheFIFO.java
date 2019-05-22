package org.apache.oro.util;

public final class CacheFIFO extends GenericCache {
   private int __curent;

   public CacheFIFO(int var1) {
      super(var1);
      this.__curent = 0;
   }

   public CacheFIFO() {
      this(20);
   }

   public final synchronized void addElement(Object var1, Object var2) {
      Object var3 = this._table.get(var1);
      if (var3 != null) {
         GenericCacheEntry var4 = (GenericCacheEntry)var3;
         var4._value = var2;
         var4._key = var1;
      } else {
         int var5;
         if (!this.isFull()) {
            var5 = this._numEntries++;
         } else {
            var5 = this.__curent;
            if (++this.__curent >= this._cache.length) {
               this.__curent = 0;
            }

            this._table.remove(this._cache[var5]._key);
         }

         this._cache[var5]._value = var2;
         this._cache[var5]._key = var1;
         this._table.put(var1, this._cache[var5]);
      }
   }
}
