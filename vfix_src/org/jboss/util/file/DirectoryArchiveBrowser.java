package org.jboss.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectoryArchiveBrowser implements Iterator {
   private Iterator files;

   public DirectoryArchiveBrowser(File file, ArchiveBrowser.Filter filter) {
      ArrayList list = new ArrayList();

      try {
         create(list, file, filter);
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }

      this.files = list.iterator();
   }

   public static void create(List list, File dir, ArchiveBrowser.Filter filter) throws Exception {
      File[] files = dir.listFiles();

      for(int i = 0; i < files.length; ++i) {
         if (files[i].isDirectory()) {
            create(list, files[i], filter);
         } else if (filter.accept(files[i].getAbsolutePath())) {
            list.add(files[i]);
         }
      }

   }

   public boolean hasNext() {
      return this.files.hasNext();
   }

   public Object next() {
      File fp = (File)this.files.next();

      try {
         return new FileInputStream(fp);
      } catch (FileNotFoundException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void remove() {
      throw new RuntimeException("Illegal operation call");
   }
}
