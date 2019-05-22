package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.jf.dexlib2.writer.IndexSection;
import org.jf.util.ExceptionWithContext;

public abstract class BaseIndexPool<Key> extends BasePool<Key, Integer> implements IndexSection<Key> {
   public BaseIndexPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   @Nonnull
   public Collection<? extends Entry<? extends Key, Integer>> getItems() {
      return this.internedItems.entrySet();
   }

   public int getItemIndex(@Nonnull Key key) {
      Integer index = (Integer)this.internedItems.get(key);
      if (index == null) {
         throw new ExceptionWithContext("Item not found.: %s", new Object[]{this.getItemString(key)});
      } else {
         return index;
      }
   }

   @Nonnull
   protected String getItemString(@Nonnull Key key) {
      return key.toString();
   }
}
