package org.jf.dexlib2.writer;

import java.util.Collection;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public interface OffsetSection<Key> {
   int getItemOffset(@Nonnull Key var1);

   @Nonnull
   Collection<? extends Entry<? extends Key, Integer>> getItems();
}
