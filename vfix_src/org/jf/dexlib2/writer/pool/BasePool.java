package org.jf.dexlib2.writer.pool;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;

public class BasePool<Key, Value> implements Markable {
   @Nonnull
   protected final DexPool dexPool;
   @Nonnull
   protected final Map<Key, Value> internedItems = Maps.newLinkedHashMap();
   private int markedItemCount = -1;

   public BasePool(@Nonnull DexPool dexPool) {
      this.dexPool = dexPool;
   }

   public void mark() {
      this.markedItemCount = this.internedItems.size();
   }

   public void reset() {
      if (this.markedItemCount < 0) {
         throw new IllegalStateException("mark() must be called before calling reset()");
      } else if (this.markedItemCount != this.internedItems.size()) {
         Iterator<Key> keys = this.internedItems.keySet().iterator();

         for(int i = 0; i < this.markedItemCount; ++i) {
            keys.next();
         }

         while(keys.hasNext()) {
            keys.next();
            keys.remove();
         }

      }
   }

   public int getItemCount() {
      return this.internedItems.size();
   }
}
