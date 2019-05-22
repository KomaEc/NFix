package org.jf.dexlib2.writer;

import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public interface IndexSection<Key> {
   int getItemIndex(@Nonnull Key var1);

   @Nonnull
   Collection<? extends Entry<? extends Key, Integer>> getItems();

   int getItemCount();
}
