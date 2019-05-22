package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.jf.dexlib2.writer.OffsetSection;
import org.jf.util.ExceptionWithContext;

public abstract class BaseOffsetPool<Key> extends BasePool<Key, Integer> implements OffsetSection<Key> {
   public BaseOffsetPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   @Nonnull
   public Collection<? extends Entry<? extends Key, Integer>> getItems() {
      return this.internedItems.entrySet();
   }

   public int getItemOffset(@Nonnull Key key) {
      Integer offset = (Integer)this.internedItems.get(key);
      if (offset == null) {
         throw new ExceptionWithContext("Item not found.: %s", new Object[]{this.getItemString(key)});
      } else {
         return offset;
      }
   }

   @Nonnull
   protected String getItemString(@Nonnull Key key) {
      return key.toString();
   }
}
