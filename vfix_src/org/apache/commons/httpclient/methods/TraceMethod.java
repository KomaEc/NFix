package org.apache.commons.httpclient.methods;

import org.apache.commons.httpclient.HttpMethodBase;

public class TraceMethod extends HttpMethodBase {
   public TraceMethod(String uri) {
      super(uri);
      this.setFollowRedirects(false);
   }

   public String getName() {
      return "TRACE";
   }

   public void recycle() {
      super.recycle();
      this.setFollowRedirects(false);
   }
}
