package org.apache.commons.httpclient;

public class DefaultMethodRetryHandler implements MethodRetryHandler {
   private int retryCount = 3;
   private boolean requestSentRetryEnabled = false;

   public boolean retryMethod(HttpMethod method, HttpConnection connection, HttpRecoverableException recoverableException, int executionCount, boolean requestSent) {
      return (!requestSent || this.requestSentRetryEnabled) && executionCount <= this.retryCount;
   }

   public boolean isRequestSentRetryEnabled() {
      return this.requestSentRetryEnabled;
   }

   public int getRetryCount() {
      return this.retryCount;
   }

   public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
      this.requestSentRetryEnabled = requestSentRetryEnabled;
   }

   public void setRetryCount(int retryCount) {
      this.retryCount = retryCount;
   }
}
