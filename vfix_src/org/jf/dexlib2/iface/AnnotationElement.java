package org.jf.dexlib2.iface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;

public interface AnnotationElement extends Comparable<AnnotationElement> {
   @Nonnull
   String getName();

   @Nonnull
   EncodedValue getValue();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(AnnotationElement var1);
}
