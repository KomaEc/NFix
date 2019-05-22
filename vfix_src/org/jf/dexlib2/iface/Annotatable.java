package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;

public interface Annotatable {
   @Nonnull
   Set<? extends Annotation> getAnnotations();
}
