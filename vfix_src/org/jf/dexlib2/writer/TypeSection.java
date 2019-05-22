package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.TypeReference;

public interface TypeSection<StringKey, TypeKey, TypeRef extends TypeReference> extends NullableIndexSection<TypeKey> {
   @Nonnull
   StringKey getString(@Nonnull TypeKey var1);

   int getItemIndex(@Nonnull TypeRef var1);
}
