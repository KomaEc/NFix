package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TypeListSection<TypeKey, TypeListKey> extends NullableOffsetSection<TypeListKey> {
   int getNullableItemOffset(@Nullable TypeListKey var1);

   @Nonnull
   Collection<? extends TypeKey> getTypes(@Nullable TypeListKey var1);
}
