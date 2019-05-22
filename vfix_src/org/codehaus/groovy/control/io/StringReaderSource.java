package org.codehaus.groovy.control.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.codehaus.groovy.control.CompilerConfiguration;

public class StringReaderSource extends AbstractReaderSource {
   private final String string;

   public StringReaderSource(String string, CompilerConfiguration configuration) {
      super(configuration);
      this.string = string;
   }

   public Reader getReader() throws IOException {
      return new StringReader(this.string);
   }
}
