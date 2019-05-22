package org.netbeans.lib.cvsclient.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import org.netbeans.lib.cvsclient.request.GzipStreamRequest;
import org.netbeans.lib.cvsclient.request.Request;

public class GzippedFileHandler extends DefaultFileHandler {
   private boolean isCompressed;

   public Request[] getInitialisationRequests() {
      return new Request[]{new GzipStreamRequest()};
   }

   protected Reader getProcessedReader(File var1) throws IOException {
      return new InputStreamReader(new GZIPInputStream(new FileInputStream(var1)));
   }

   protected InputStream getProcessedInputStream(File var1) throws IOException {
      return new GZIPInputStream(new FileInputStream(var1));
   }
}
