package org.jf.dexlib2.iface.value;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.BasicAnnotation;

public interface AnnotationEncodedValue extends EncodedValue, BasicAnnotation {
   @Nonnull
   String getType();

   @Nonnull
   Set<? extends AnnotationElement> getElements();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull EncodedValue var1);
}
