package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/** @deprecated */
public class Deltree extends Task {
   private File dir;

   public void setDir(File dir) {
      this.dir = dir;
   }

   public void execute() throws BuildException {
      this.log("DEPRECATED - The deltree task is deprecated.  Use delete instead.");
      if (this.dir == null) {
         throw new BuildException("dir attribute must be set!", this.getLocation());
      } else {
         if (this.dir.exists()) {
            if (!this.dir.isDirectory()) {
               if (!this.dir.delete()) {
                  throw new BuildException("Unable to delete directory " + this.dir.getAbsolutePath(), this.getLocation());
               }

               return;
            }

            this.log("Deleting: " + this.dir.getAbsolutePath());

            try {
               this.removeDir(this.dir);
            } catch (IOException var3) {
               String msg = "Unable to delete " + this.dir.getAbsolutePath();
               throw new BuildException(msg, this.getLocation());
            }
         }

      }
   }

   private void removeDir(File dir) throws IOException {
      String[] list = dir.list();

      for(int i = 0; i < list.length; ++i) {
         String s = list[i];
         File f = new File(dir, s);
         if (f.isDirectory()) {
            this.removeDir(f);
         } else if (!f.delete()) {
            throw new BuildException("Unable to delete file " + f.getAbsolutePath());
         }
      }

      if (!dir.delete()) {
         throw new BuildException("Unable to delete directory " + dir.getAbsolutePath());
      }
   }
}
