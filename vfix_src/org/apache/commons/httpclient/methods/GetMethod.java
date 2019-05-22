package org.apache.commons.httpclient.methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMethod extends HttpMethodBase {
   private static final Log LOG;
   /** @deprecated */
   private static final String TEMP_DIR = "temp/";
   /** @deprecated */
   private File fileData;
   /** @deprecated */
   private String tempDir;
   /** @deprecated */
   private String tempFile;
   /** @deprecated */
   private boolean useDisk;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$GetMethod;

   public GetMethod() {
      this.tempDir = "temp/";
      this.tempFile = null;
      this.useDisk = false;
      this.setFollowRedirects(true);
   }

   public GetMethod(String uri) {
      super(uri);
      this.tempDir = "temp/";
      this.tempFile = null;
      this.useDisk = false;
      LOG.trace("enter GetMethod(String)");
      this.setFollowRedirects(true);
   }

   /** @deprecated */
   public GetMethod(String path, String tempDir) {
      super(path);
      this.tempDir = "temp/";
      this.tempFile = null;
      this.useDisk = false;
      LOG.trace("enter GetMethod(String, String)");
      this.setUseDisk(true);
      this.setTempDir(tempDir);
      this.setFollowRedirects(true);
   }

   /** @deprecated */
   public GetMethod(String path, String tempDir, String tempFile) {
      super(path);
      this.tempDir = "temp/";
      this.tempFile = null;
      this.useDisk = false;
      LOG.trace("enter GetMethod(String, String, String)");
      this.setUseDisk(true);
      this.setTempDir(tempDir);
      this.setTempFile(tempFile);
      this.setFollowRedirects(true);
   }

   /** @deprecated */
   public GetMethod(String path, File fileData) {
      this(path);
      LOG.trace("enter GetMethod(String, File)");
      this.useDisk = true;
      this.fileData = fileData;
      this.setFollowRedirects(true);
   }

   /** @deprecated */
   public void setFileData(File fileData) {
      this.checkNotUsed();
      this.fileData = fileData;
   }

   /** @deprecated */
   public File getFileData() {
      return this.fileData;
   }

   public String getName() {
      return "GET";
   }

   /** @deprecated */
   public void setTempDir(String tempDir) {
      this.checkNotUsed();
      this.tempDir = tempDir;
      this.setUseDisk(true);
   }

   /** @deprecated */
   public String getTempDir() {
      return this.tempDir;
   }

   /** @deprecated */
   public void setTempFile(String tempFile) {
      this.checkNotUsed();
      this.tempFile = tempFile;
   }

   /** @deprecated */
   public String getTempFile() {
      return this.tempFile;
   }

   /** @deprecated */
   public void setUseDisk(boolean useDisk) {
      this.checkNotUsed();
      this.useDisk = useDisk;
   }

   /** @deprecated */
   public boolean getUseDisk() {
      return this.useDisk;
   }

   /** @deprecated */
   public void recycle() {
      LOG.trace("enter GetMethod.recycle()");
      super.recycle();
      this.fileData = null;
      this.setFollowRedirects(true);
   }

   protected void readResponseBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter GetMethod.readResponseBody(HttpState, HttpConnection)");
      super.readResponseBody(state, conn);
      OutputStream out = null;
      if (this.useDisk) {
         out = new FileOutputStream(this.createTempFile());
         InputStream in = this.getResponseBodyAsStream();
         byte[] buffer = new byte[10000];

         int len;
         while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
         }

         in.close();
         out.close();
         this.setResponseStream(new FileInputStream(this.createTempFile()));
      }

   }

   /** @deprecated */
   private File createTempFile() {
      if (this.fileData == null) {
         File dir = new File(this.tempDir);
         dir.deleteOnExit();
         dir.mkdirs();
         String tempFileName = null;
         if (this.tempFile == null) {
            String encodedPath = URLEncoder.encode(this.getPath());
            int length = encodedPath.length();
            if (length > 200) {
               encodedPath = encodedPath.substring(length - 190, length);
            }

            tempFileName = System.currentTimeMillis() + "-" + encodedPath + ".tmp";
         } else {
            tempFileName = this.tempFile;
         }

         this.fileData = new File(this.tempDir, tempFileName);
         this.fileData = new File(this.tempDir, tempFileName);
         this.fileData.deleteOnExit();
      }

      return this.fileData;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$GetMethod == null ? (class$org$apache$commons$httpclient$methods$GetMethod = class$("org.apache.commons.httpclient.methods.GetMethod")) : class$org$apache$commons$httpclient$methods$GetMethod);
   }
}
