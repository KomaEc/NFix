package org.apache.commons.httpclient;

import java.io.IOException;

public class URIException extends IOException {
   public static final int UNKNOWN = 0;
   public static final int PARSING = 1;
   public static final int UNSUPPORTED_ENCODING = 2;
   public static final int ESCAPING = 3;
   public static final int PUNYCODE = 4;
   protected int reasonCode;
   protected String reason;

   public URIException() {
   }

   public URIException(int reasonCode) {
      this.setReasonCode(reasonCode);
   }

   public URIException(int reasonCode, String reason) {
      super(reason);
      this.reason = reason;
      this.setReasonCode(reasonCode);
   }

   public URIException(String reason) {
      super(reason);
      this.reason = reason;
      this.setReasonCode(0);
   }

   public int getReasonCode() {
      return this.reasonCode;
   }

   public void setReasonCode(int reasonCode) {
      this.reasonCode = reasonCode;
   }

   public String getReason() {
      return this.reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }
}
