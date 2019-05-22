package org.jboss.net.protocol.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FileURLConnection extends URLConnection {
   static boolean decodeFilePaths = true;
   static boolean useURI = true;
   protected final File file;

   public FileURLConnection(URL url) throws IOException {
      super(url);

      try {
         if (useURI) {
            this.file = new File(url.toURI());
         } else {
            String path = url.getPath();
            if (decodeFilePaths) {
               path = URLDecoder.decode(path, "UTF-8");
            }

            this.file = new File(path.replace('/', File.separatorChar).replace('|', ':'));
         }

         super.doOutput = false;
      } catch (URISyntaxException var4) {
         IOException ioe = new IOException();
         ioe.initCause(var4);
         throw ioe;
      }
   }

   public File getFile() {
      return this.file;
   }

   public void connect() throws IOException {
      if (!this.connected) {
         if (!this.file.exists()) {
            throw new FileNotFoundException(this.file.getPath());
         } else {
            this.connected = true;
         }
      }
   }

   public InputStream getInputStream() throws IOException {
      this.connect();
      if (!this.file.isDirectory()) {
         return new FileInputStream(this.file);
      } else {
         String[] files = this.file.list();
         Arrays.sort(files);
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < files.length; ++i) {
            sb.append(files[i]).append("\n");
         }

         return new ByteArrayInputStream(sb.toString().getBytes());
      }
   }

   public OutputStream getOutputStream() throws IOException {
      this.connect();
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         FilePermission p = new FilePermission(this.file.getPath(), "write");
         sm.checkPermission(p);
      }

      return new FileOutputStream(this.file);
   }

   public String getHeaderField(String name) {
      String headerField = null;
      if (name.equalsIgnoreCase("last-modified")) {
         long lastModified = this.getLastModified();
         if (lastModified != 0L) {
            Date modifiedDate = new Date(lastModified);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            headerField = sdf.format(modifiedDate);
         }
      } else if (name.equalsIgnoreCase("content-length")) {
         headerField = String.valueOf(this.file.length());
      } else if (name.equalsIgnoreCase("content-type")) {
         if (this.file.isDirectory()) {
            headerField = "text/plain";
         } else {
            headerField = getFileNameMap().getContentTypeFor(this.file.getName());
            if (headerField == null) {
               try {
                  InputStream is = this.getInputStream();
                  BufferedInputStream bis = new BufferedInputStream(is);
                  headerField = URLConnection.guessContentTypeFromStream(bis);
                  bis.close();
               } catch (IOException var7) {
               }
            }
         }
      } else if (name.equalsIgnoreCase("date")) {
         headerField = String.valueOf(this.getLastModified());
      } else {
         headerField = super.getHeaderField(name);
      }

      return headerField;
   }

   public Permission getPermission() throws IOException {
      return new FilePermission(this.file.getPath(), "read");
   }

   public long getLastModified() {
      return this.file.lastModified();
   }

   static {
      String flag = System.getProperty("org.jboss.net.protocol.file.decodeFilePaths");
      if (flag != null) {
         decodeFilePaths = Boolean.valueOf(flag);
      }

      flag = System.getProperty("org.jboss.net.protocol.file.useURI");
      if (flag != null) {
         useURI = Boolean.valueOf(flag);
      }

   }
}
