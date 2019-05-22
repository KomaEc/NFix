package org.jboss.util.file;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

public class FileProtocolArchiveBrowserFactory implements ArchiveBrowserFactory {
   public Iterator create(URL url, ArchiveBrowser.Filter filter) {
      File f = new File(url.getPath());
      return (Iterator)(f.isDirectory() ? new DirectoryArchiveBrowser(f, filter) : new JarArchiveBrowser(f, filter));
   }
}
