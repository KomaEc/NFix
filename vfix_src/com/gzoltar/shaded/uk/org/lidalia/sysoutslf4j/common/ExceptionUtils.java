package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public final class ExceptionUtils {
   public static RuntimeException asRuntimeException(Throwable throwable) {
      if (throwable == null) {
         throw new IllegalArgumentException("Throwable argument cannot be null");
      } else if (throwable instanceof Error) {
         throw (Error)throwable;
      } else {
         Object result;
         if (throwable instanceof RuntimeException) {
            result = (RuntimeException)throwable;
         } else {
            if (throwable instanceof InterruptedException || throwable instanceof InterruptedIOException) {
               throw new IllegalArgumentException("An interrupted exception needs to be handled to end the thread, or the interrupted status needs to be restored, or the exception needs to be propagated explicitly - it should not be used as an argument to this method", throwable);
            }

            if (throwable instanceof InvocationTargetException) {
               result = asRuntimeException(throwable.getCause());
            } else {
               result = new WrappedCheckedException(throwable);
            }
         }

         return (RuntimeException)result;
      }
   }

   public static <ResultType> ResultType doUnchecked(Callable<ResultType> work) {
      try {
         return work.call();
      } catch (Exception var2) {
         throw asRuntimeException(var2);
      }
   }

   private ExceptionUtils() {
      throw new UnsupportedOperationException("Not instantiable");
   }
}
