package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class GZip extends Pack {
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$GZip;

   protected void pack() {
      GZIPOutputStream zOut = null;

      try {
         zOut = new GZIPOutputStream(new FileOutputStream(this.zipFile));
         this.zipResource(this.getSrcResource(), zOut);
      } catch (IOException var7) {
         String msg = "Problem creating gzip " + var7.getMessage();
         throw new BuildException(msg, var7, this.getLocation());
      } finally {
         FileUtils.close((OutputStream)zOut);
      }

   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$GZip == null ? (class$org$apache$tools$ant$taskdefs$GZip = class$("org.apache.tools.ant.taskdefs.GZip")) : class$org$apache$tools$ant$taskdefs$GZip);
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
