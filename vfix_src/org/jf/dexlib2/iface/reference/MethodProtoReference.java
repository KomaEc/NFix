package org.jf.dexlib2.iface.reference;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MethodProtoReference extends Reference, Comparable<MethodProtoReference> {
   @Nonnull
   List<? extends CharSequence> getParameterTypes();

   @Nonnull
   String getReturnType();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull MethodProtoReference var1);
}
