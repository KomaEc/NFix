package org.jf.dexlib2.base;

import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;

public abstract class BaseTryBlock<EH extends ExceptionHandler> implements TryBlock<EH> {
   public boolean equals(Object o) {
      if (!(o instanceof TryBlock)) {
         return false;
      } else {
         TryBlock other = (TryBlock)o;
         return this.getStartCodeAddress() == other.getStartCodeAddress() && this.getCodeUnitCount() == other.getCodeUnitCount() && this.getExceptionHandlers().equals(other.getExceptionHandlers());
      }
   }
}
