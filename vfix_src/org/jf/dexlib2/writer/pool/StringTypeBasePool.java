package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.NullableIndexSection;
import org.jf.util.ExceptionWithContext;

public abstract class StringTypeBasePool extends BasePool<String, Integer> implements NullableIndexSection<CharSequence>, Markable {
   public StringTypeBasePool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   @Nonnull
   public Collection<Entry<String, Integer>> getItems() {
      return this.internedItems.entrySet();
   }

   public int getItemIndex(@Nonnull CharSequence key) {
      Integer index = (Integer)this.internedItems.get(key.toString());
      if (index == null) {
         throw new ExceptionWithContext("Item not found.: %s", new Object[]{key.toString()});
      } else {
         return index;
      }
   }

   public int getNullableItemIndex(@Nullable CharSequence key) {
      return key == null ? -1 : this.getItemIndex(key);
   }
}
