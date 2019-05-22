package org.apache.commons.httpclient.cookie;

import org.apache.commons.httpclient.HttpException;

public class MalformedCookieException extends HttpException {
   public MalformedCookieException() {
   }

   public MalformedCookieException(String message) {
      super(message);
   }
}
