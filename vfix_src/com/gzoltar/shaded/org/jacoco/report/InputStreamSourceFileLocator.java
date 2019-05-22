package com.gzoltar.shaded.org.jacoco.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class InputStreamSourceFileLocator implements ISourceFileLocator {
   private final String encoding;
   private final int tabWidth;

   protected InputStreamSourceFileLocator(String encoding, int tabWidth) {
      this.encoding = encoding;
      this.tabWidth = tabWidth;
   }

   public Reader getSourceFile(String packageName, String fileName) throws IOException {
      InputStream in;
      if (packageName.length() > 0) {
         in = this.getSourceStream(packageName + "/" + fileName);
      } else {
         in = this.getSourceStream(fileName);
      }

      if (in == null) {
         return null;
      } else {
         return this.encoding == null ? new InputStreamReader(in) : new InputStreamReader(in, this.encoding);
      }
   }

   public int getTabWidth() {
      return this.tabWidth;
   }

   protected abstract InputStream getSourceStream(String var1) throws IOException;
}
