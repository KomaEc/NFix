package org.jf.dexlib2.iface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.TypeReference;

public interface ExceptionHandler extends Comparable<ExceptionHandler> {
   @Nullable
   String getExceptionType();

   @Nullable
   TypeReference getExceptionTypeReference();

   int getHandlerCodeAddress();

   int hashCode();

   boolean equals(@Nullable Object var1);

   int compareTo(@Nonnull ExceptionHandler var1);
}
