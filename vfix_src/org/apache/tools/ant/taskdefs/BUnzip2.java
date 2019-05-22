package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2InputStream;

public class BUnzip2 extends Unpack {
   private static final String DEFAULT_EXTENSION = ".bz2";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$BUnzip2;

   protected String getDefaultExtension() {
      return ".bz2";
   }

   protected void extract() {
      if (this.source.lastModified() > this.dest.lastModified()) {
         this.log("Expanding " + this.source.getAbsolutePath() + " to " + this.dest.getAbsolutePath());
         FileOutputStream out = null;
         CBZip2InputStream zIn = null;
         InputStream fis = null;
         BufferedInputStream bis = null;

         try {
            out = new FileOutputStream(this.dest);
            fis = this.srcResource.getInputStream();
            bis = new BufferedInputStream(fis);
            int b = bis.read();
            if (b != 66) {
               throw new BuildException("Invalid bz2 file.", this.getLocation());
            }

            b = bis.read();
            if (b != 90) {
               throw new BuildException("Invalid bz2 file.", this.getLocation());
            }

            zIn = new CBZip2InputStream(bis);
            byte[] buffer = new byte[8192];
            int count = 0;

            do {
               out.write(buffer, 0, count);
               count = zIn.read(buffer, 0, buffer.length);
            } while(count != -1);
         } catch (IOException var11) {
            String msg = "Problem expanding bzip2 " + var11.getMessage();
            throw new BuildException(msg, var11, this.getLocation());
         } finally {
            FileUtils.close((InputStream)bis);
            FileUtils.close(fis);
            FileUtils.close((OutputStream)out);
            FileUtils.close((InputStream)zIn);
         }
      }

   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$BUnzip2 == null ? (class$org$apache$tools$ant$taskdefs$BUnzip2 = class$("org.apache.tools.ant.taskdefs.BUnzip2")) : class$org$apache$tools$ant$taskdefs$BUnzip2);
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
