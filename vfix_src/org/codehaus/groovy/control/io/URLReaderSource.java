package org.codehaus.groovy.control.io;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import org.codehaus.groovy.control.CompilerConfiguration;

public class URLReaderSource extends AbstractReaderSource {
   private URL url;

   public URLReaderSource(URL url, CompilerConfiguration configuration) {
      super(configuration);
      this.url = url;
   }

   public Reader getReader() throws IOException {
      return new InputStreamReader(this.url.openStream(), this.configuration.getSourceEncoding());
   }
}
