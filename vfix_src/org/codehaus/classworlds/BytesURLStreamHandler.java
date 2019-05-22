package org.codehaus.classworlds;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class BytesURLStreamHandler extends URLStreamHandler {
   byte[] content;
   int offset;
   int length;

   public BytesURLStreamHandler(byte[] content) {
      this.content = content;
   }

   public URLConnection openConnection(URL url) {
      return new BytesURLConnection(url, this.content);
   }
}
