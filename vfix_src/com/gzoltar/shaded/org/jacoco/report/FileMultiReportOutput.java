package com.gzoltar.shaded.org.jacoco.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileMultiReportOutput implements IMultiReportOutput {
   private final File basedir;

   public FileMultiReportOutput(File basedir) {
      this.basedir = basedir;
   }

   public OutputStream createFile(String path) throws IOException {
      File file = new File(this.basedir, path);
      File parent = file.getParentFile();
      parent.mkdirs();
      if (!parent.isDirectory()) {
         throw new IOException(String.format("Can't create directory %s.", parent));
      } else {
         return new BufferedOutputStream(new FileOutputStream(file));
      }
   }

   public void close() throws IOException {
   }
}
