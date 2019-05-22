package org.netbeans.lib.cvsclient.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Date;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.request.Request;
import org.netbeans.lib.cvsclient.util.BugLog;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class DefaultFileHandler implements FileHandler {
   private static final boolean DEBUG = false;
   private static final int CHUNK_SIZE = 32768;
   private Date modifiedDate;
   private TransmitTextFilePreprocessor transmitTextFilePreprocessor;
   private WriteTextFilePreprocessor writeTextFilePreprocessor;
   private WriteTextFilePreprocessor writeRcsDiffFilePreprocessor;
   private GlobalOptions globalOptions;

   public DefaultFileHandler() {
      this.setTransmitTextFilePreprocessor(new DefaultTransmitTextFilePreprocessor());
      this.setWriteTextFilePreprocessor(new DefaultWriteTextFilePreprocessor());
      this.setWriteRcsDiffFilePreprocessor(new WriteRcsDiffFilePreprocessor());
   }

   public TransmitTextFilePreprocessor getTransmitTextFilePreprocessor() {
      return this.transmitTextFilePreprocessor;
   }

   public void setTransmitTextFilePreprocessor(TransmitTextFilePreprocessor var1) {
      this.transmitTextFilePreprocessor = var1;
   }

   public WriteTextFilePreprocessor getWriteTextFilePreprocessor() {
      return this.writeTextFilePreprocessor;
   }

   public void setWriteTextFilePreprocessor(WriteTextFilePreprocessor var1) {
      this.writeTextFilePreprocessor = var1;
   }

   public WriteTextFilePreprocessor getWriteRcsDiffFilePreprocessor() {
      return this.writeRcsDiffFilePreprocessor;
   }

   public void setWriteRcsDiffFilePreprocessor(WriteTextFilePreprocessor var1) {
      this.writeRcsDiffFilePreprocessor = var1;
   }

   protected String getLengthString(long var1) {
      return var1 + "\n";
   }

   protected Reader getProcessedReader(File var1) throws IOException {
      return new FileReader(var1);
   }

   protected InputStream getProcessedInputStream(File var1) throws IOException {
      return new FileInputStream(var1);
   }

   public Request[] getInitialisationRequests() {
      return null;
   }

   public void transmitTextFile(File var1, LoggedDataOutputStream var2) throws IOException {
      if (var1 != null && var1.exists()) {
         File var3 = var1;
         TransmitTextFilePreprocessor var4 = this.getTransmitTextFilePreprocessor();
         if (var4 != null) {
            var3 = var4.getPreprocessedTextFile(var1);
         }

         BufferedInputStream var5 = null;

         try {
            long var6 = var3.length();
            var2.writeBytes(this.getLengthString(var6), "US-ASCII");
            var5 = new BufferedInputStream(new FileInputStream(var3));
            byte[] var8 = new byte['耀'];

            while(var6 > 0L) {
               int var9 = var6 >= 32768L ? '耀' : (int)var6;
               int var10 = var5.read(var8, 0, var9);
               if (var10 == -1) {
                  throw new IOException("Unexpected end of stream from " + var3 + ".");
               }

               var6 -= (long)var10;
               var2.write(var8, 0, var10);
            }

            var2.flush();
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (IOException var17) {
               }
            }

            if (var4 != null) {
               var4.cleanup(var3);
            }

         }
      } else {
         throw new IllegalArgumentException("File is either null or does not exist. Cannot transmit.");
      }
   }

   public void transmitBinaryFile(File var1, LoggedDataOutputStream var2) throws IOException {
      if (var1 != null && var1.exists()) {
         BufferedInputStream var3 = null;

         try {
            var3 = new BufferedInputStream(new FileInputStream(var1));
            long var4 = var1.length();
            var2.writeBytes(this.getLengthString(var4), "US-ASCII");
            byte[] var6 = new byte['耀'];

            while(var4 > 0L) {
               int var7 = var4 >= 32768L ? '耀' : (int)var4;
               int var8 = var3.read(var6, 0, var7);
               if (var8 == -1) {
                  throw new IOException("Unexpected end of stream from " + var1 + ".");
               }

               var4 -= (long)var8;
               var2.write(var6, 0, var8);
            }

            var2.flush();
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var15) {
                  var15.printStackTrace();
               }
            }

         }
      } else {
         throw new IllegalArgumentException("File is either null or does not exist. Cannot transmit.");
      }
   }

   public void writeTextFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException {
      this.writeAndPostProcessTextFile(var1, var2, var3, var4, this.getWriteTextFilePreprocessor());
   }

   public void writeRcsDiffFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException {
      this.writeAndPostProcessTextFile(var1, var2, var3, var4, this.getWriteRcsDiffFilePreprocessor());
   }

   private void writeAndPostProcessTextFile(String var1, String var2, LoggedDataInputStream var3, int var4, WriteTextFilePreprocessor var5) throws IOException {
      File var6 = new File(var1);
      boolean var7 = this.resetReadOnly(var6);
      this.createNewFile(var6);
      File var8 = File.createTempFile("cvsCRLF", "tmp");

      try {
         BufferedOutputStream var9 = null;

         try {
            var9 = new BufferedOutputStream(new FileOutputStream(var8));
            byte[] var10 = new byte['耀'];

            while(var4 > 0) {
               int var11 = var4 >= 32768 ? '耀' : var4;
               var11 = var3.read(var10, 0, var11);
               if (var11 == -1) {
                  throw new IOException("Unexpected end of stream: " + var1 + "\nMissing " + var4 + " bytes. Probably network communication failure.\nPlease try again.");
               }

               var4 -= var11;
               var9.write(var10, 0, var11);
            }
         } finally {
            if (var9 != null) {
               try {
                  var9.close();
               } catch (IOException var32) {
               }
            }

         }

         InputStream var36 = this.getProcessedInputStream(var8);

         try {
            var5.copyTextFileToLocation(var36, var6, new DefaultFileHandler.StreamProvider(var6));
         } finally {
            var36.close();
         }

         if (this.modifiedDate != null) {
            var6.setLastModified(this.modifiedDate.getTime());
            this.modifiedDate = null;
         }
      } finally {
         var8.delete();
      }

      if (var7) {
         FileUtils.setFileReadOnly(var6, true);
      }

   }

   public void writeBinaryFile(String var1, String var2, LoggedDataInputStream var3, int var4) throws IOException {
      File var5 = new File(var1);
      boolean var6 = this.resetReadOnly(var5);
      this.createNewFile(var5);
      File var7 = new File(var5.getParentFile(), "CVS");
      var7.mkdir();
      File var8 = File.createTempFile("cvsPostConversion", "tmp", var7);

      try {
         BufferedOutputStream var9 = new BufferedOutputStream(new FileOutputStream(var8));
         byte[] var10 = new byte['耀'];

         int var12;
         try {
            while(var4 > 0) {
               int var11 = var4 >= 32768 ? '耀' : var4;
               var12 = var3.read(var10, 0, var11);
               if (var12 == -1) {
                  throw new IOException("Unexpected end of stream: " + var1 + "\nMissing " + var4 + " bytes. Probably network communication failure.\nPlease try again.");
               }

               if (var12 < 0) {
                  break;
               }

               var4 -= var12;
               var9.write(var10, 0, var12);
            }
         } finally {
            var9.close();
         }

         BufferedInputStream var28 = new BufferedInputStream(this.getProcessedInputStream(var8));
         var9 = new BufferedOutputStream(this.createOutputStream(var5));

         try {
            for(var12 = var28.read(var10, 0, 32768); var12 > 0; var12 = var28.read(var10, 0, 32768)) {
               var9.write(var10, 0, var12);
            }
         } finally {
            var9.close();
            var28.close();
         }

         if (this.modifiedDate != null) {
            var5.setLastModified(this.modifiedDate.getTime());
            this.modifiedDate = null;
         }
      } finally {
         var8.delete();
      }

      if (var6) {
         FileUtils.setFileReadOnly(var5, true);
      }

   }

   protected boolean createNewFile(File var1) throws IOException {
      var1.getParentFile().mkdirs();
      return var1.createNewFile();
   }

   protected OutputStream createOutputStream(File var1) throws IOException {
      return new FileOutputStream(var1);
   }

   private boolean resetReadOnly(File var1) throws IOException {
      boolean var2 = this.globalOptions != null && this.globalOptions.isCheckedOutFilesReadOnly();
      if (var1.exists() && var2) {
         var2 = !var1.canWrite();
         if (var2) {
            FileUtils.setFileReadOnly(var1, false);
         }
      }

      return var2;
   }

   public void removeLocalFile(String var1) throws IOException {
      File var2 = new File(var1);
      if (var2.exists() && !var2.delete()) {
         System.err.println("Could not delete file " + var2.getAbsolutePath());
      }

   }

   public void renameLocalFile(String var1, String var2) throws IOException {
      File var3 = new File(var1);
      File var4 = new File(var3.getParentFile(), var2);
      var3.renameTo(var4);
   }

   public void setNextFileDate(Date var1) {
      this.modifiedDate = var1;
   }

   public void setGlobalOptions(GlobalOptions var1) {
      BugLog.getInstance().assertNotNull(var1);
      this.globalOptions = var1;
      this.transmitTextFilePreprocessor.setTempDir(var1.getTempDir());
   }

   private class StreamProvider implements OutputStreamProvider {
      private final File file;

      public StreamProvider(File var2) {
         this.file = var2;
      }

      public OutputStream createOutputStream() throws IOException {
         return DefaultFileHandler.this.createOutputStream(this.file);
      }
   }
}
