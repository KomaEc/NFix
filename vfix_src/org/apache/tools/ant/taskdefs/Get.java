package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

public class Get extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private URL source;
   private File dest;
   private boolean verbose = false;
   private boolean useTimestamp = false;
   private boolean ignoreErrors = false;
   private String uname = null;
   private String pword = null;

   public void execute() throws BuildException {
      int logLevel = 2;
      Get.DownloadProgress progress = null;
      if (this.verbose) {
         progress = new Get.VerboseProgress(System.out);
      }

      try {
         this.doGet(logLevel, progress);
      } catch (IOException var4) {
         this.log("Error getting " + this.source + " to " + this.dest);
         if (!this.ignoreErrors) {
            throw new BuildException(var4, this.getLocation());
         }
      }

   }

   public boolean doGet(int logLevel, Get.DownloadProgress progress) throws IOException {
      if (this.source == null) {
         throw new BuildException("src attribute is required", this.getLocation());
      } else if (this.dest == null) {
         throw new BuildException("dest attribute is required", this.getLocation());
      } else if (this.dest.exists() && this.dest.isDirectory()) {
         throw new BuildException("The specified destination is a directory", this.getLocation());
      } else if (this.dest.exists() && !this.dest.canWrite()) {
         throw new BuildException("Can't write to " + this.dest.getAbsolutePath(), this.getLocation());
      } else {
         if (progress == null) {
            progress = new Get.NullProgress();
         }

         this.log("Getting: " + this.source, logLevel);
         this.log("To: " + this.dest.getAbsolutePath(), logLevel);
         long timestamp = 0L;
         boolean hasTimestamp = false;
         if (this.useTimestamp && this.dest.exists()) {
            timestamp = this.dest.lastModified();
            if (this.verbose) {
               Date t = new Date(timestamp);
               this.log("local file date : " + t.toString(), logLevel);
            }

            hasTimestamp = true;
         }

         URLConnection connection = this.source.openConnection();
         if (hasTimestamp) {
            connection.setIfModifiedSince(timestamp);
         }

         if (this.uname != null || this.pword != null) {
            String up = this.uname + ":" + this.pword;
            Get.Base64Converter encoder = new Get.Base64Converter();
            String encoding = encoder.encode(up.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoding);
         }

         connection.connect();
         if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection)connection;
            long lastModified = httpConnection.getLastModified();
            if (httpConnection.getResponseCode() == 304 || lastModified != 0L && hasTimestamp && timestamp >= lastModified) {
               this.log("Not modified - so not downloaded", logLevel);
               return false;
            }

            if (httpConnection.getResponseCode() == 401) {
               String message = "HTTP Authorization failure";
               if (this.ignoreErrors) {
                  this.log(message, logLevel);
                  return false;
               }

               throw new BuildException(message);
            }
         }

         InputStream is = null;
         int i = 0;

         while(i < 3) {
            try {
               is = connection.getInputStream();
               break;
            } catch (IOException var20) {
               this.log("Error opening connection " + var20, logLevel);
               ++i;
            }
         }

         if (is == null) {
            this.log("Can't get " + this.source + " to " + this.dest, logLevel);
            if (this.ignoreErrors) {
               return false;
            } else {
               throw new BuildException("Can't get " + this.source + " to " + this.dest, this.getLocation());
            }
         } else {
            FileOutputStream fos = new FileOutputStream(this.dest);
            ((Get.DownloadProgress)progress).beginDownload();
            boolean finished = false;

            try {
               byte[] buffer = new byte[102400];

               while(true) {
                  int length;
                  if ((length = is.read(buffer)) < 0) {
                     finished = true;
                     break;
                  }

                  fos.write(buffer, 0, length);
                  ((Get.DownloadProgress)progress).onTick();
               }
            } finally {
               FileUtils.close((OutputStream)fos);
               FileUtils.close(is);
               if (!finished) {
                  this.dest.delete();
               }

            }

            ((Get.DownloadProgress)progress).endDownload();
            if (this.useTimestamp) {
               long remoteTimestamp = connection.getLastModified();
               if (this.verbose) {
                  Date t = new Date(remoteTimestamp);
                  this.log("last modified = " + t.toString() + (remoteTimestamp == 0L ? " - using current time instead" : ""), logLevel);
               }

               if (remoteTimestamp != 0L) {
                  FILE_UTILS.setFileLastModified(this.dest, remoteTimestamp);
               }
            }

            return true;
         }
      }
   }

   public void setSrc(URL u) {
      this.source = u;
   }

   public void setDest(File dest) {
      this.dest = dest;
   }

   public void setVerbose(boolean v) {
      this.verbose = v;
   }

   public void setIgnoreErrors(boolean v) {
      this.ignoreErrors = v;
   }

   public void setUseTimestamp(boolean v) {
      this.useTimestamp = v;
   }

   public void setUsername(String u) {
      this.uname = u;
   }

   public void setPassword(String p) {
      this.pword = p;
   }

   public static class VerboseProgress implements Get.DownloadProgress {
      private int dots = 0;
      PrintStream out;

      public VerboseProgress(PrintStream out) {
         this.out = out;
      }

      public void beginDownload() {
         this.dots = 0;
      }

      public void onTick() {
         this.out.print(".");
         if (this.dots++ > 50) {
            this.out.flush();
            this.dots = 0;
         }

      }

      public void endDownload() {
         this.out.println();
         this.out.flush();
      }
   }

   public static class NullProgress implements Get.DownloadProgress {
      public void beginDownload() {
      }

      public void onTick() {
      }

      public void endDownload() {
      }
   }

   public interface DownloadProgress {
      void beginDownload();

      void onTick();

      void endDownload();
   }

   protected static class Base64Converter extends org.apache.tools.ant.util.Base64Converter {
   }
}
