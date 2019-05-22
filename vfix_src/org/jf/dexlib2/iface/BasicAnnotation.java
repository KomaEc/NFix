package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;

public interface BasicAnnotation {
   @Nonnull
   String getType();

   @Nonnull
   Set<? extends AnnotationElement> getElements();
}
