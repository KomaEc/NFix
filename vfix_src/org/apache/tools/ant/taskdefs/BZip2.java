package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2OutputStream;

public class BZip2 extends Pack {
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$BZip2;

   protected void pack() {
      CBZip2OutputStream zOut = null;

      try {
         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.zipFile));
         bos.write(66);
         bos.write(90);
         zOut = new CBZip2OutputStream(bos);
         this.zipResource(this.getSrcResource(), zOut);
      } catch (IOException var7) {
         String msg = "Problem creating bzip2 " + var7.getMessage();
         throw new BuildException(msg, var7, this.getLocation());
      } finally {
         FileUtils.close((OutputStream)zOut);
      }

   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$BZip2 == null ? (class$org$apache$tools$ant$taskdefs$BZip2 = class$("org.apache.tools.ant.taskdefs.BZip2")) : class$org$apache$tools$ant$taskdefs$BZip2);
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
