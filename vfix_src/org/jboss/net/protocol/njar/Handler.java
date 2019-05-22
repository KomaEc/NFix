package org.jboss.net.protocol.njar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import org.jboss.logging.Logger;
import org.jboss.util.ThrowableHandler;
import org.jboss.util.stream.Streams;

public class Handler extends URLStreamHandler {
   public static final String PROTOCOL = "njar";
   public static final String NJAR_SEPARATOR = "^/";
   public static final String JAR_SEPARATOR = "!/";
   private static final Logger log = Logger.getLogger(Handler.class);
   protected Map savedJars = new HashMap();

   public URLConnection openConnection(URL url) throws IOException {
      String file = url.getFile();
      String embeddedURL = file;
      String jarPath = "";
      boolean trace = log.isTraceEnabled();
      int pos = file.lastIndexOf("^/");
      if (pos >= 0) {
         embeddedURL = file.substring(0, pos);
         if (file.length() > pos + "^/".length()) {
            jarPath = file.substring(pos + "^/".length());
         }
      }

      if (embeddedURL.startsWith("njar")) {
         if (trace) {
            log.trace("Opening next  nested jar: " + embeddedURL);
         }

         File tempJar = (File)this.savedJars.get(embeddedURL);
         if (tempJar == null) {
            URLConnection embededDataConnection = (new URL(embeddedURL)).openConnection();
            if (trace) {
               log.trace("Content length: " + embededDataConnection.getContentLength());
            }

            InputStream embededData = embededDataConnection.getInputStream();
            tempJar = File.createTempFile("nested-", ".jar");
            tempJar.deleteOnExit();
            if (trace) {
               log.trace("temp file location : " + tempJar);
            }

            FileOutputStream output = new FileOutputStream(tempJar);

            try {
               long bytes = Streams.copyb(embededData, output);
               if (trace) {
                  log.trace("copied " + bytes + " bytes");
               }
            } finally {
               Streams.flush((OutputStream)output);
               Streams.close(embededData);
               Streams.close((OutputStream)output);
            }

            this.savedJars.put(embeddedURL, tempJar);
         }

         String t = tempJar.getCanonicalFile().toURL().toExternalForm();
         if (trace) {
            log.trace("file URL : " + t);
         }

         t = "njar:" + t + "^/" + jarPath;
         if (trace) {
            log.trace("Opening saved jar: " + t);
         }

         URL u = new URL(t);
         if (trace) {
            log.trace("Using URL: " + u);
         }

         return u.openConnection();
      } else {
         if (trace) {
            log.trace("Opening final nested jar: " + embeddedURL);
         }

         URL u = new URL("jar:" + embeddedURL + "!/" + jarPath);
         if (trace) {
            log.trace("Using URL: " + u);
         }

         return u.openConnection();
      }
   }

   public static URL njarToFile(URL url) {
      if (url.getProtocol().equals("njar")) {
         try {
            URL dummy = new URL("njar:" + url.toString() + "^/" + "dummy.jar");
            String tmp = dummy.openConnection().getURL().toString();
            tmp = tmp.substring("jar:".length());
            tmp = tmp.substring(0, tmp.length() - "!/dummy.jar".length());
            return new URL(tmp);
         } catch (Exception var3) {
            ThrowableHandler.addWarning(var3);
         }
      }

      return url;
   }
}
