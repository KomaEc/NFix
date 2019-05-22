package org.apache.tools.ant.types;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.ZipResource;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipScanner extends ArchiveScanner {
   protected void fillMapsFromArchive(Resource src, String encoding, Map fileEntries, Map matchFileEntries, Map dirEntries, Map matchDirEntries) {
      ZipEntry entry = null;
      ZipFile zf = null;
      File srcFile = null;
      if (src instanceof FileResource) {
         srcFile = ((FileResource)src).getFile();

         try {
            try {
               zf = new ZipFile(srcFile, encoding);
            } catch (ZipException var21) {
               throw new BuildException("problem reading " + srcFile, var21);
            } catch (IOException var22) {
               throw new BuildException("problem opening " + srcFile, var22);
            }

            Enumeration e = zf.getEntries();

            while(e.hasMoreElements()) {
               entry = (ZipEntry)e.nextElement();
               Resource r = new ZipResource(srcFile, encoding, entry);
               String name = entry.getName();
               if (entry.isDirectory()) {
                  name = trimSeparator(name);
                  dirEntries.put(name, r);
                  if (this.match(name)) {
                     matchDirEntries.put(name, r);
                  }
               } else {
                  fileEntries.put(name, r);
                  if (this.match(name)) {
                     matchFileEntries.put(name, r);
                  }
               }
            }
         } finally {
            if (zf != null) {
               try {
                  zf.close();
               } catch (IOException var20) {
               }
            }

         }

      } else {
         throw new BuildException("only file resources are supported");
      }
   }
}
