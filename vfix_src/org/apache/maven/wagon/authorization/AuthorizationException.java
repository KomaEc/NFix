package org.apache.maven.wagon.authorization;

import org.apache.maven.wagon.WagonException;

public class AuthorizationException extends WagonException {
   public AuthorizationException(String message) {
      super(message);
   }

   public AuthorizationException(String message, Throwable cause) {
      super(message, cause);
   }
}
