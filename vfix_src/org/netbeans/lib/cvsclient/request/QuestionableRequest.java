package org.netbeans.lib.cvsclient.request;

public final class QuestionableRequest extends Request {
   private String questionFile;

   public QuestionableRequest(String var1) {
      this.questionFile = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.questionFile == null) {
         throw new UnconfiguredRequestException("Questionable request has not been configured");
      } else {
         return "Questionable " + this.questionFile + "\n";
      }
   }

   public boolean isResponseExpected() {
      return false;
   }
}
