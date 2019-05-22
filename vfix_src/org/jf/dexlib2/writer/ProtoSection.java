package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ProtoSection<StringKey, TypeKey, ProtoKey, TypeListKey> extends IndexSection<ProtoKey> {
   @Nonnull
   StringKey getShorty(@Nonnull ProtoKey var1);

   @Nonnull
   TypeKey getReturnType(@Nonnull ProtoKey var1);

   @Nullable
   TypeListKey getParameters(@Nonnull ProtoKey var1);
}
