package org.jboss.net.protocol.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
   public URLConnection openConnection(URL url) throws IOException {
      return new FileURLConnection(url);
   }

   protected void parseURL(URL url, String s, int i, int j) {
      super.parseURL(url, s.replace(File.separatorChar, '/'), i, j);
   }
}
