package org.jboss.net.protocol.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.jboss.logging.Logger;
import org.jboss.net.protocol.URLLister;
import org.jboss.net.protocol.URLListerBase;

public class FileURLLister extends URLListerBase {
   private static final Logger log = Logger.getLogger(FileURLLister.class);

   public Collection listMembers(URL baseUrl, URLLister.URLFilter filter) throws IOException {
      return this.listMembers(baseUrl, filter, false);
   }

   public Collection<URL> listMembers(URL baseUrl, URLLister.URLFilter filter, boolean scanNonDottedSubDirs) throws IOException {
      String baseUrlString = baseUrl.toString();
      if (!baseUrlString.endsWith("/")) {
         throw new IOException("Does not end with '/', not a directory url: " + baseUrlString);
      } else {
         File dir = new File(baseUrl.getPath());
         if (!dir.isDirectory()) {
            throw new FileNotFoundException("Not pointing to a directory, url: " + baseUrlString);
         } else {
            ArrayList<URL> resultList = new ArrayList();
            this.listFiles(baseUrl, filter, scanNonDottedSubDirs, resultList);
            return resultList;
         }
      }
   }

   private void listFiles(final URL baseUrl, final URLLister.URLFilter filter, boolean scanNonDottedSubDirs, ArrayList<URL> resultList) throws IOException {
      final File baseDir = new File(baseUrl.getPath());
      String[] filenames = baseDir.list(new FilenameFilter() {
         public boolean accept(File dir, String name) {
            try {
               return filter.accept(baseUrl, name);
            } catch (Exception var4) {
               FileURLLister.log.debug("Unexpected exception filtering entry '" + name + "' in directory '" + baseDir + "'", var4);
               return true;
            }
         }
      });
      if (filenames == null) {
         throw new IOException("Could not list directory '" + baseDir + "', reason unknown");
      } else {
         String baseUrlString = baseUrl.toString();

         for(int i = 0; i < filenames.length; ++i) {
            String filename = filenames[i];
            File file = new File(baseDir, filename);
            boolean isDir = file.isDirectory();
            URL subUrl = this.createURL(baseUrlString, filename, isDir);
            if (scanNonDottedSubDirs && isDir && filename.indexOf(46) == -1) {
               this.listFiles(subUrl, filter, scanNonDottedSubDirs, resultList);
            } else {
               resultList.add(subUrl);
            }
         }

      }
   }

   private URL createURL(String baseUrlString, String filename, boolean isDirectory) {
      try {
         return new URL(baseUrlString + filename + (isDirectory ? "/" : ""));
      } catch (MalformedURLException var5) {
         throw new IllegalStateException();
      }
   }
}
