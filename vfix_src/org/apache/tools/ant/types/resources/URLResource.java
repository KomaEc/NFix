package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;

public class URLResource extends Resource {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final int NULL_URL = Resource.getMagicNumber("null URL".getBytes());
   private URL url;
   private URLConnection conn;

   public URLResource() {
   }

   public URLResource(URL u) {
      this.setURL(u);
   }

   public URLResource(File f) {
      this.setFile(f);
   }

   public URLResource(String u) {
      this(newURL(u));
   }

   public synchronized void setURL(URL u) {
      this.checkAttributesAllowed();
      this.url = u;
   }

   public synchronized void setFile(File f) {
      try {
         this.setURL(FILE_UTILS.getFileURL(f));
      } catch (MalformedURLException var3) {
         throw new BuildException(var3);
      }
   }

   public synchronized URL getURL() {
      return this.isReference() ? ((URLResource)this.getCheckedRef()).getURL() : this.url;
   }

   public synchronized void setRefid(Reference r) {
      if (this.url != null) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   public synchronized String getName() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).getName() : this.getURL().getFile().substring(1);
   }

   public synchronized String toString() {
      return this.isReference() ? this.getCheckedRef().toString() : String.valueOf(this.getURL());
   }

   public synchronized boolean isExists() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).isExists() : this.isExists(false);
   }

   private synchronized boolean isExists(boolean closeConnection) {
      if (this.getURL() == null) {
         return false;
      } else {
         boolean var3;
         try {
            this.connect();
            boolean var2 = true;
            return var2;
         } catch (IOException var7) {
            var3 = false;
         } finally {
            if (closeConnection) {
               this.close();
            }

         }

         return var3;
      }
   }

   public synchronized long getLastModified() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getLastModified();
      } else {
         return !this.isExists(false) ? 0L : this.conn.getLastModified();
      }
   }

   public synchronized boolean isDirectory() {
      return this.isReference() ? ((Resource)this.getCheckedRef()).isDirectory() : this.getName().endsWith("/");
   }

   public synchronized long getSize() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getSize();
      } else if (!this.isExists(false)) {
         return 0L;
      } else {
         try {
            this.connect();
            long contentlength = (long)this.conn.getContentLength();
            this.close();
            return contentlength;
         } catch (IOException var4) {
            return -1L;
         }
      }
   }

   public synchronized boolean equals(Object another) {
      if (this == another) {
         return true;
      } else if (this.isReference()) {
         return this.getCheckedRef().equals(another);
      } else if (!another.getClass().equals(this.getClass())) {
         return false;
      } else {
         URLResource otheru = (URLResource)another;
         return this.getURL() == null ? otheru.getURL() == null : this.getURL().equals(otheru.getURL());
      }
   }

   public synchronized int hashCode() {
      return this.isReference() ? this.getCheckedRef().hashCode() : MAGIC * (this.getURL() == null ? NULL_URL : this.getURL().hashCode());
   }

   public synchronized InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         this.connect();

         InputStream var1;
         try {
            var1 = this.conn.getInputStream();
         } finally {
            this.conn = null;
         }

         return var1;
      }
   }

   public synchronized OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else {
         this.connect();

         OutputStream var1;
         try {
            var1 = this.conn.getOutputStream();
         } finally {
            this.conn = null;
         }

         return var1;
      }
   }

   protected synchronized void connect() throws IOException {
      URL u = this.getURL();
      if (u == null) {
         throw new BuildException("URL not set");
      } else {
         if (this.conn == null) {
            try {
               this.conn = u.openConnection();
               this.conn.connect();
            } catch (IOException var3) {
               this.log(var3.toString(), 0);
               this.conn = null;
               throw var3;
            }
         }

      }
   }

   private synchronized void close() {
      if (this.conn != null) {
         try {
            if (this.conn instanceof JarURLConnection) {
               JarURLConnection juc = (JarURLConnection)this.conn;
               JarFile jf = juc.getJarFile();
               jf.close();
               jf = null;
            } else if (this.conn instanceof HttpURLConnection) {
               ((HttpURLConnection)this.conn).disconnect();
            }
         } catch (IOException var6) {
         } finally {
            this.conn = null;
         }
      }

   }

   private static URL newURL(String u) {
      try {
         return new URL(u);
      } catch (MalformedURLException var2) {
         throw new BuildException(var2);
      }
   }
}
