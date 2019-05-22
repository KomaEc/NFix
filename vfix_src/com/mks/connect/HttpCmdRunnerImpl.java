package com.mks.connect;

import com.mks.api.response.APIException;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;

class HttpCmdRunnerImpl extends AbstractCmdRunner {
   protected UserApplicationSessionImpl uas;
   protected URI url;
   protected HttpClient httpClient;
   protected boolean autoReconnect;

   HttpCmdRunnerImpl(UserApplicationSessionImpl uas, HttpClient client) {
      super(uas);
      this.uas = uas;
      this.httpClient = client;
      this.autoReconnect = uas.getAutoReconnect();
   }

   protected BlimpInputStream createBlimpStream(String[] cmd, boolean generateSubRtns) {
      BlimpInputStream bis = new HttpBlimpInputStream(this, cmd, this.httpClient);
      bis.setCodePage("UTF-8");
      bis.setGenerateSubRoutines(generateSubRtns);
      return bis;
   }

   protected synchronized URI getSessionURI() throws IOException {
      if (this.url == null) {
         this.url = this.uas.getSession(this.uas.getSessionURI());
      }

      return this.url;
   }

   protected synchronized void invalidateURI() {
      this.uas.invalidateURI();
      this.url = null;
   }

   protected boolean getAutoReconnect() {
      return this.autoReconnect;
   }

   public void release() throws APIException {
      try {
         if (this.isFinished()) {
            this.uas.removeConnection(this);
         }

         super.release();
      } finally {
         UserApplicationSessionImpl.releaseHttpClient(this.httpClient);
      }

   }
}
