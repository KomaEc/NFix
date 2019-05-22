package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Annotation extends BasicAnnotation, Comparable<Annotation> {
   int getVisibility();

   @Nonnull
   String getType();

   @Nonnull
   Set<? extends AnnotationElement> getElements();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(Annotation var1);
}
