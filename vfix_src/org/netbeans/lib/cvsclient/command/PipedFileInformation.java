package org.netbeans.lib.cvsclient.command;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PipedFileInformation extends FileInfoContainer {
   private File file;
   private String repositoryRevision;
   private String repositoryFileName;
   private File tempFile;
   private OutputStream tmpStream;

   public PipedFileInformation(File var1) {
      this.tempFile = var1;

      try {
         this.tmpStream = new BufferedOutputStream(new FileOutputStream(var1));
      } catch (IOException var3) {
      }

   }

   public File getFile() {
      return this.file;
   }

   protected void setFile(File var1) {
      this.file = var1;
   }

   public String getRepositoryRevision() {
      return this.repositoryRevision;
   }

   protected void setRepositoryRevision(String var1) {
      this.repositoryRevision = var1;
   }

   public String getRepositoryFileName() {
      return this.repositoryFileName;
   }

   protected void setRepositoryFileName(String var1) {
      this.repositoryFileName = var1;
   }

   protected void addToTempFile(byte[] var1) throws IOException {
      if (this.tmpStream != null) {
         this.tmpStream.write(var1);
      }

   }

   public void addToTempFile(byte[] var1, int var2) throws IOException {
      if (this.tmpStream != null) {
         this.tmpStream.write(var1, 0, var2);
      }

   }

   protected void closeTempFile() throws IOException {
      if (this.tmpStream != null) {
         this.tmpStream.flush();
         this.tmpStream.close();
      }

   }

   public File getTempFile() {
      return this.tempFile;
   }
}
