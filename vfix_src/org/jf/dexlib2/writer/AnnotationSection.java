package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;

public interface AnnotationSection<StringKey, TypeKey, AnnotationKey, AnnotationElement, EncodedValue> extends OffsetSection<AnnotationKey> {
   int getVisibility(@Nonnull AnnotationKey var1);

   @Nonnull
   TypeKey getType(@Nonnull AnnotationKey var1);

   @Nonnull
   Collection<? extends AnnotationElement> getElements(@Nonnull AnnotationKey var1);

   @Nonnull
   StringKey getElementName(@Nonnull AnnotationElement var1);

   @Nonnull
   EncodedValue getElementValue(@Nonnull AnnotationElement var1);
}
