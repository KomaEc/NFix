package org.jboss.util.file;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Iterator;

public class JarProtocolArchiveBrowserFactory implements ArchiveBrowserFactory {
   public Iterator create(URL url, ArchiveBrowser.Filter filter) {
      if (url.toString().endsWith("!/")) {
         try {
            return new JarArchiveBrowser((JarURLConnection)url.openConnection(), filter);
         } catch (IOException var4) {
            throw new RuntimeException("Unable to browse url: " + url, var4);
         }
      } else {
         try {
            return new JarStreamBrowser(url.openStream(), filter);
         } catch (IOException var5) {
            throw new RuntimeException("Unable to browse url: " + url, var5);
         }
      }
   }
}
