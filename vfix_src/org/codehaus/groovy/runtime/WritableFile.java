package org.codehaus.groovy.runtime;

import groovy.lang.Writable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class WritableFile extends File implements Writable {
   private final String encoding;

   public WritableFile(File delegate) {
      this(delegate, (String)null);
   }

   public WritableFile(File delegate, String encoding) {
      super(delegate.toURI());
      this.encoding = encoding;
   }

   public Writer writeTo(Writer out) throws IOException {
      BufferedReader reader = this.encoding == null ? DefaultGroovyMethods.newReader((File)this) : DefaultGroovyMethods.newReader((File)this, this.encoding);

      try {
         for(int c = reader.read(); c != -1; c = reader.read()) {
            out.write(c);
         }
      } finally {
         reader.close();
      }

      return out;
   }
}
