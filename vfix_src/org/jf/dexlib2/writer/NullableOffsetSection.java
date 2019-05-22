package org.jf.dexlib2.writer;

import javax.annotation.Nullable;

public interface NullableOffsetSection<Key> extends OffsetSection<Key> {
   int getNullableItemOffset(@Nullable Key var1);
}
