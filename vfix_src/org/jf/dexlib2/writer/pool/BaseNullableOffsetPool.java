package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.NullableOffsetSection;

public abstract class BaseNullableOffsetPool<Key> extends BaseOffsetPool<Key> implements NullableOffsetSection<Key> {
   public BaseNullableOffsetPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public int getNullableItemOffset(@Nullable Key key) {
      return key == null ? 0 : this.getItemOffset(key);
   }
}
