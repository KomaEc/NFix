package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class Untar extends Expand {
   private Untar.UntarCompressionMethod compression = new Untar.UntarCompressionMethod();

   public void setCompression(Untar.UntarCompressionMethod method) {
      this.compression = method;
   }

   public void setEncoding(String encoding) {
      throw new BuildException("The " + this.getTaskName() + " task doesn't support the encoding" + " attribute", this.getLocation());
   }

   protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
      FileInputStream fis = null;

      try {
         fis = new FileInputStream(srcF);
         this.expandStream(srcF.getPath(), fis, dir);
      } catch (IOException var9) {
         throw new BuildException("Error while expanding " + srcF.getPath(), var9, this.getLocation());
      } finally {
         FileUtils.close((InputStream)fis);
      }

   }

   protected void expandResource(Resource srcR, File dir) {
      InputStream i = null;

      try {
         i = srcR.getInputStream();
         this.expandStream(srcR.getName(), i, dir);
      } catch (IOException var8) {
         throw new BuildException("Error while expanding " + srcR.getName(), var8, this.getLocation());
      } finally {
         FileUtils.close(i);
      }

   }

   private void expandStream(String name, InputStream stream, File dir) throws IOException {
      TarInputStream tis = null;

      try {
         tis = new TarInputStream(this.compression.decompress(name, new BufferedInputStream(stream)));
         this.log("Expanding: " + name + " into " + dir, 2);
         TarEntry te = null;
         FileNameMapper mapper = this.getMapper();

         while((te = tis.getNextEntry()) != null) {
            this.extractFile(FileUtils.getFileUtils(), (File)null, dir, tis, te.getName(), te.getModTime(), te.isDirectory(), mapper);
         }

         this.log("expand complete", 3);
      } finally {
         FileUtils.close((InputStream)tis);
      }
   }

   public static final class UntarCompressionMethod extends EnumeratedAttribute {
      private static final String NONE = "none";
      private static final String GZIP = "gzip";
      private static final String BZIP2 = "bzip2";

      public UntarCompressionMethod() {
         this.setValue("none");
      }

      public String[] getValues() {
         return new String[]{"none", "gzip", "bzip2"};
      }

      public InputStream decompress(String name, InputStream istream) throws IOException, BuildException {
         String v = this.getValue();
         if ("gzip".equals(v)) {
            return new GZIPInputStream(istream);
         } else if ("bzip2".equals(v)) {
            char[] magic = new char[]{'B', 'Z'};

            for(int i = 0; i < magic.length; ++i) {
               if (istream.read() != magic[i]) {
                  throw new BuildException("Invalid bz2 file." + name);
               }
            }

            return new CBZip2InputStream(istream);
         } else {
            return istream;
         }
      }
   }
}
