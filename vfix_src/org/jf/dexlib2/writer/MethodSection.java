package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public interface MethodSection<StringKey, TypeKey, ProtoRefKey extends MethodProtoReference, MethodRefKey extends MethodReference, MethodKey> extends IndexSection<MethodRefKey> {
   @Nonnull
   MethodRefKey getMethodReference(@Nonnull MethodKey var1);

   @Nonnull
   TypeKey getDefiningClass(@Nonnull MethodRefKey var1);

   @Nonnull
   ProtoRefKey getPrototype(@Nonnull MethodRefKey var1);

   @Nonnull
   ProtoRefKey getPrototype(@Nonnull MethodKey var1);

   @Nonnull
   StringKey getName(@Nonnull MethodRefKey var1);

   int getMethodIndex(@Nonnull MethodKey var1);
}
