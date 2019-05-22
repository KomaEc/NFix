package org.jf.dexlib2.writer;

import javax.annotation.Nullable;

public interface NullableIndexSection<Key> extends IndexSection<Key> {
   int getNullableItemIndex(@Nullable Key var1);
}
