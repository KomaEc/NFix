package org.apache.maven.wagon.authentication;

import org.apache.maven.wagon.WagonException;

public class AuthenticationException extends WagonException {
   public AuthenticationException(String message) {
      super(message);
   }

   public AuthenticationException(String message, Throwable cause) {
      super(message, cause);
   }
}
