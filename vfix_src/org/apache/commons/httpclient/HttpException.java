package org.apache.commons.httpclient;

public class HttpException extends URIException {
   public HttpException() {
   }

   public HttpException(String message) {
      super(message);
   }
}
