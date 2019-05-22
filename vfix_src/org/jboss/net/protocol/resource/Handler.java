package org.jboss.net.protocol.resource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
   public URLConnection openConnection(URL url) throws IOException {
      return new ResourceURLConnection(url);
   }
}
