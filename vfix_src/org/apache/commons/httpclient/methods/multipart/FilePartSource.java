package org.apache.commons.httpclient.methods.multipart;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FilePartSource implements PartSource {
   private File file;
   private String fileName;

   public FilePartSource(File file) throws FileNotFoundException {
      this.file = null;
      this.fileName = null;
      this.file = file;
      if (file != null) {
         if (!file.isFile()) {
            throw new FileNotFoundException("File is not a normal file.");
         }

         if (!file.canRead()) {
            throw new FileNotFoundException("File is not readable.");
         }

         this.fileName = file.getName();
      }

   }

   public FilePartSource(String fileName, File file) throws FileNotFoundException {
      this(file);
      if (fileName != null) {
         this.fileName = fileName;
      }

   }

   public long getLength() {
      return this.file != null ? this.file.length() : 0L;
   }

   public String getFileName() {
      return this.fileName == null ? "noname" : this.fileName;
   }

   public InputStream createInputStream() throws IOException {
      return (InputStream)(this.file != null ? new FileInputStream(this.file) : new ByteArrayInputStream(new byte[0]));
   }
}
