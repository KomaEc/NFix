package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class GUnzip extends Unpack {
   private static final String DEFAULT_EXTENSION = ".gz";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$GUnzip;

   protected String getDefaultExtension() {
      return ".gz";
   }

   protected void extract() {
      if (this.source.lastModified() > this.dest.lastModified()) {
         this.log("Expanding " + this.source.getAbsolutePath() + " to " + this.dest.getAbsolutePath());
         FileOutputStream out = null;
         GZIPInputStream zIn = null;
         InputStream fis = null;

         try {
            out = new FileOutputStream(this.dest);
            fis = this.srcResource.getInputStream();
            zIn = new GZIPInputStream(fis);
            byte[] buffer = new byte[8192];
            int count = 0;

            do {
               out.write(buffer, 0, count);
               count = zIn.read(buffer, 0, buffer.length);
            } while(count != -1);
         } catch (IOException var9) {
            String msg = "Problem expanding gzip " + var9.getMessage();
            throw new BuildException(msg, var9, this.getLocation());
         } finally {
            FileUtils.close(fis);
            FileUtils.close((OutputStream)out);
            FileUtils.close((InputStream)zIn);
         }
      }

   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$GUnzip == null ? (class$org$apache$tools$ant$taskdefs$GUnzip = class$("org.apache.tools.ant.taskdefs.GUnzip")) : class$org$apache$tools$ant$taskdefs$GUnzip);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
