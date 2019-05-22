package com.google.inject.internal;

class Exceptions {
   public static RuntimeException rethrowCause(Throwable throwable) {
      Throwable cause = throwable;
      if (throwable.getCause() != null) {
         cause = throwable.getCause();
      }

      return rethrow(cause);
   }

   public static RuntimeException rethrow(Throwable throwable) {
      if (throwable instanceof RuntimeException) {
         throw (RuntimeException)throwable;
      } else if (throwable instanceof Error) {
         throw (Error)throwable;
      } else {
         throw new Exceptions.UnhandledCheckedUserException(throwable);
      }
   }

   static class UnhandledCheckedUserException extends RuntimeException {
      public UnhandledCheckedUserException(Throwable cause) {
         super(cause);
      }
   }
}
