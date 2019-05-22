package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class IsSigned extends DataType implements Condition {
   private static final String SIG_START = "META-INF/";
   private static final String SIG_END = ".SF";
   private static final int SHORT_SIG_LIMIT = 8;
   private String name;
   private File file;

   public void setFile(File file) {
      this.file = file;
   }

   public void setName(String name) {
      this.name = name;
   }

   public static boolean isSigned(File zipFile, String name) throws IOException {
      ZipFile jarFile = null;

      boolean longSig;
      try {
         jarFile = new ZipFile(zipFile);
         boolean var5;
         if (null != name) {
            boolean shortSig = jarFile.getEntry("META-INF/" + name.toUpperCase() + ".SF") != null;
            longSig = false;
            if (name.length() > 8) {
               longSig = jarFile.getEntry("META-INF/" + name.substring(0, 8).toUpperCase() + ".SF") != null;
            }

            var5 = shortSig || longSig;
            return var5;
         }

         Enumeration entries = jarFile.getEntries();

         while(entries.hasMoreElements()) {
            String eName = ((ZipEntry)entries.nextElement()).getName();
            if (eName.startsWith("META-INF/") && eName.endsWith(".SF")) {
               var5 = true;
               return var5;
            }
         }

         longSig = false;
      } finally {
         ZipFile.closeQuietly(jarFile);
      }

      return longSig;
   }

   public boolean eval() {
      if (this.file == null) {
         throw new BuildException("The file attribute must be set.");
      } else if (this.file != null && !this.file.exists()) {
         this.log("The file \"" + this.file.getAbsolutePath() + "\" does not exist.", 3);
         return false;
      } else {
         boolean r = false;

         try {
            r = isSigned(this.file, this.name);
         } catch (IOException var3) {
            this.log("Got IOException reading file \"" + this.file.getAbsolutePath() + "\"" + var3, 1);
         }

         if (r) {
            this.log("File \"" + this.file.getAbsolutePath() + "\" is signed.", 3);
         }

         return r;
      }
   }
}
