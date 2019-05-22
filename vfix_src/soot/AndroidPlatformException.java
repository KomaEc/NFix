package soot;

public class AndroidPlatformException extends RuntimeException {
   private static final long serialVersionUID = 5582559536663042315L;

   public AndroidPlatformException() {
   }

   public AndroidPlatformException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }

   public AndroidPlatformException(String message, Throwable cause) {
      super(message, cause);
   }

   public AndroidPlatformException(String message) {
      super(message);
   }

   public AndroidPlatformException(Throwable cause) {
      super(cause);
   }
}
