package org.jf.dexlib2.iface;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TryBlock<EH extends ExceptionHandler> {
   int getStartCodeAddress();

   int getCodeUnitCount();

   @Nonnull
   List<? extends EH> getExceptionHandlers();

   boolean equals(@Nullable Object var1);
}
