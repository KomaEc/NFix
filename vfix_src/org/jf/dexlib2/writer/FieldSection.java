package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.FieldReference;

public interface FieldSection<StringKey, TypeKey, FieldRefKey extends FieldReference, FieldKey> extends IndexSection<FieldRefKey> {
   @Nonnull
   TypeKey getDefiningClass(@Nonnull FieldRefKey var1);

   @Nonnull
   TypeKey getFieldType(@Nonnull FieldRefKey var1);

   @Nonnull
   StringKey getName(@Nonnull FieldRefKey var1);

   int getFieldIndex(@Nonnull FieldKey var1);
}
