package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.HttpException;

public class MalformedChallengeException extends HttpException {
   public MalformedChallengeException() {
   }

   public MalformedChallengeException(String message) {
      super(message);
   }
}
