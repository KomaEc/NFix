package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.iface.reference.TypeReference;

public interface MethodParameter extends TypeReference, LocalInfo {
   @Nonnull
   String getType();

   @Nonnull
   Set<? extends Annotation> getAnnotations();

   @Nullable
   String getName();

   @Nullable
   String getSignature();
}
