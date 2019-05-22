package org.codehaus.groovy.control.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.groovy.control.CompilerConfiguration;

public class InputStreamReaderSource extends AbstractReaderSource {
   private InputStream stream;

   public InputStreamReaderSource(InputStream stream, CompilerConfiguration configuration) {
      super(configuration);
      this.stream = stream;
   }

   public Reader getReader() throws IOException {
      if (this.stream != null) {
         Reader reader = new InputStreamReader(this.stream, this.configuration.getSourceEncoding());
         this.stream = null;
         return reader;
      } else {
         return null;
      }
   }

   public boolean canReopenSource() {
      return false;
   }
}
