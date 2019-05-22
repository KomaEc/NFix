package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.HttpException;

public class AuthenticationException extends HttpException {
   public AuthenticationException() {
   }

   public AuthenticationException(String message) {
      super(message);
   }
}
