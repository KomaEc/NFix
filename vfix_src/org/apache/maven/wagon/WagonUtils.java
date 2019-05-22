package org.apache.maven.wagon;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.codehaus.plexus.util.FileUtils;

/** @deprecated */
public final class WagonUtils {
   private WagonUtils() {
   }

   public static String toString(String resource, Wagon wagon) throws IOException, TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      File file = null;
      boolean var9 = false;

      String var4;
      try {
         var9 = true;
         file = File.createTempFile("wagon", "tmp");
         wagon.get(resource, file);
         String var3 = FileUtils.fileRead(file);
         var4 = var3;
         var9 = false;
      } finally {
         if (var9) {
            if (file != null) {
               boolean deleted = file.delete();
               if (!deleted) {
                  file.deleteOnExit();
               }
            }

         }
      }

      if (file != null) {
         boolean deleted = file.delete();
         if (!deleted) {
            file.deleteOnExit();
         }
      }

      return var4;
   }

   public static void putDirectory(File dir, Wagon wagon, boolean includeBasdir) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException {
      LinkedList queue = new LinkedList();
      if (includeBasdir) {
         queue.add(dir.getName());
      } else {
         queue.add("");
      }

      while(!queue.isEmpty()) {
         String path = (String)queue.removeFirst();
         File currentDir = new File(dir, path);
         File[] files = currentDir.listFiles();

         for(int i = 0; i < files.length; ++i) {
            File file = files[i];
            String resource;
            if (path.length() > 0) {
               resource = path + "/" + file.getName();
            } else {
               resource = file.getName();
            }

            if (file.isDirectory()) {
               queue.add(resource);
            } else {
               wagon.put(file, resource);
            }
         }
      }

   }
}
