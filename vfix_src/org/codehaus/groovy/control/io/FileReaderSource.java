package org.codehaus.groovy.control.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.codehaus.groovy.control.CompilerConfiguration;

public class FileReaderSource extends AbstractReaderSource {
   private File file;
   private final Charset UTF8 = Charset.forName("UTF-8");

   public FileReaderSource(File file, CompilerConfiguration configuration) {
      super(configuration);
      this.file = file;
   }

   public Reader getReader() throws IOException {
      Charset cs = Charset.forName(this.configuration.getSourceEncoding());
      InputStream in = new BufferedInputStream(new FileInputStream(this.file));
      if (this.UTF8.name().equalsIgnoreCase(cs.name())) {
         in.mark(3);
         boolean hasBOM = true;

         try {
            int i = in.read();
            hasBOM &= i == 239;
            i = in.read();
            hasBOM &= i == 187;
            i = in.read();
            hasBOM &= i == 255;
         } catch (IOException var5) {
            hasBOM = false;
         }

         if (!hasBOM) {
            in.reset();
         }
      }

      return new InputStreamReader(in, cs);
   }
}
