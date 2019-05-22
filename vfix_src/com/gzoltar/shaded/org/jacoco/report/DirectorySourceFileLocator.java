package com.gzoltar.shaded.org.jacoco.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DirectorySourceFileLocator extends InputStreamSourceFileLocator {
   private final File directory;

   public DirectorySourceFileLocator(File directory, String encoding, int tabWidth) {
      super(encoding, tabWidth);
      this.directory = directory;
   }

   protected InputStream getSourceStream(String path) throws IOException {
      File file = new File(this.directory, path);
      return file.exists() ? new FileInputStream(file) : null;
   }
}
