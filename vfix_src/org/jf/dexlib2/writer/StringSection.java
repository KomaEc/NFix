package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.StringReference;

public interface StringSection<StringKey, StringRef extends StringReference> extends NullableIndexSection<StringKey> {
   int getItemIndex(@Nonnull StringRef var1);

   boolean hasJumboIndexes();
}
