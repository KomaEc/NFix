package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileWriterFactory implements WriterFactory {
   private final File file;
   private PrintWriter writer;

   public FileWriterFactory(File file) {
      this.file = file;
   }

   public PrintWriter create() {
      this.file.getParentFile().mkdirs();

      try {
         if (this.writer == null) {
            this.writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8"));
         }

         return this.writer;
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public void close() {
      if (this.writer != null) {
         this.writer.close();
      }

   }
}
