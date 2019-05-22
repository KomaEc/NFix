package soot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoundFile {
   private static final Logger logger = LoggerFactory.getLogger(FoundFile.class);
   protected File file;
   protected String entryName;
   protected ZipFile zipFile;
   protected ZipEntry zipEntry;
   protected List<InputStream> openedInputStreams;

   public FoundFile(ZipFile file, ZipEntry entry) {
      this();
      if (file != null && entry != null) {
         this.zipFile = file;
         this.zipEntry = entry;
      } else {
         throw new IllegalArgumentException("Error: The archive and entry cannot be null.");
      }
   }

   public FoundFile(String archivePath, String entryName) {
      this();
      if (archivePath != null && entryName != null) {
         this.file = new File(archivePath);
         this.entryName = entryName;
      } else {
         throw new IllegalArgumentException("Error: The archive path and entry name cannot be null.");
      }
   }

   public FoundFile(File file) {
      this();
      if (file == null) {
         throw new IllegalArgumentException("Error: The file cannot be null.");
      } else {
         this.file = file;
         this.entryName = null;
      }
   }

   private FoundFile() {
      this.openedInputStreams = new ArrayList();
   }

   public String getFilePath() {
      return this.file.getPath();
   }

   public boolean isZipFile() {
      return this.entryName != null;
   }

   public File getFile() {
      return this.file;
   }

   public InputStream inputStream() {
      InputStream ret = null;
      if (!this.isZipFile()) {
         try {
            ret = new FileInputStream(this.file);
         } catch (Exception var15) {
            throw new RuntimeException("Error: Failed to open a InputStream for the file at path '" + this.file.getPath() + "'.", var15);
         }
      } else {
         if (this.zipFile == null) {
            try {
               this.zipFile = new ZipFile(this.file);
               this.zipEntry = this.zipFile.getEntry(this.entryName);
               if (this.zipEntry == null) {
                  this.silentClose();
                  throw new RuntimeException("Error: Failed to find entry '" + this.entryName + "' in the archive file at path '" + this.file.getPath() + "'.");
               }
            } catch (Exception var14) {
               this.silentClose();
               throw new RuntimeException("Error: Failed to open the archive file at path '" + this.file.getPath() + "' for entry '" + this.entryName + "'.", var14);
            }
         }

         InputStream stream = null;

         try {
            stream = this.zipFile.getInputStream(this.zipEntry);
            ret = this.doJDKBugWorkaround(stream, this.zipEntry.getSize());
         } catch (Exception var13) {
            throw new RuntimeException("Error: Failed to open a InputStream for the entry '" + this.zipEntry.getName() + "' of the archive at path '" + this.zipFile.getName() + "'.", var13);
         } finally {
            if (stream != null) {
               try {
                  stream.close();
               } catch (IOException var12) {
                  logger.debug((String)var12.getMessage(), (Throwable)var12);
               }
            }

         }
      }

      this.openedInputStreams.add(ret);
      return (InputStream)ret;
   }

   public void silentClose() {
      try {
         this.close();
      } catch (Exception var2) {
         logger.debug((String)var2.getMessage(), (Throwable)var2);
      }

   }

   public void close() {
      List<Exception> errs = new ArrayList(0);
      Iterator var2 = this.openedInputStreams.iterator();

      while(var2.hasNext()) {
         InputStream is = (InputStream)var2.next();

         try {
            is.close();
         } catch (Exception var12) {
            errs.add(var12);
         }
      }

      this.openedInputStreams.clear();
      this.closeZipFile(errs);
      if (!errs.isEmpty()) {
         String msg = null;
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         PrintStream ps = null;

         try {
            ps = new PrintStream(baos, true, "utf-8");
            ps.println("Error: Failed to close all opened resources. The following exceptions were thrown in the process: ");
            int i = 0;
            Iterator var6 = errs.iterator();

            while(var6.hasNext()) {
               Throwable t = (Throwable)var6.next();
               ps.print("Exception ");
               ps.print(i++);
               ps.print(": ");
               logger.error(t.getMessage(), t);
            }

            msg = new String(baos.toByteArray(), StandardCharsets.UTF_8);
         } catch (Exception var13) {
         } finally {
            ps.close();
         }

         throw new RuntimeException(msg);
      }
   }

   protected void closeZipFile(List<Exception> errs) {
      if (this.zipFile != null) {
         try {
            this.zipFile.close();
            errs.clear();
         } catch (Exception var3) {
            errs.add(var3);
         }

         this.zipFile = null;
         this.zipEntry = null;
      }

   }

   private InputStream doJDKBugWorkaround(InputStream is, long size) throws IOException {
      int sz = (int)size;
      byte[] buf = new byte[sz];
      int N = true;
      int ln = false;

      int ln;
      for(int count = 0; sz > 0 && (ln = is.read(buf, count, Math.min(1024, sz))) != -1; sz -= ln) {
         count += ln;
      }

      return new ByteArrayInputStream(buf);
   }
}
