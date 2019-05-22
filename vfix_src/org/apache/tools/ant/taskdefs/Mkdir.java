package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Mkdir extends Task {
   private static final int MKDIR_RETRY_SLEEP_MILLIS = 10;
   private File dir;

   public void execute() throws BuildException {
      if (this.dir == null) {
         throw new BuildException("dir attribute is required", this.getLocation());
      } else if (this.dir.isFile()) {
         throw new BuildException("Unable to create directory as a file already exists with that name: " + this.dir.getAbsolutePath());
      } else {
         if (!this.dir.exists()) {
            boolean result = this.mkdirs(this.dir);
            if (!result) {
               String msg = "Directory " + this.dir.getAbsolutePath() + " creation was not successful for an unknown reason";
               throw new BuildException(msg, this.getLocation());
            }

            this.log("Created dir: " + this.dir.getAbsolutePath());
         } else {
            this.log("Skipping " + this.dir.getAbsolutePath() + " because it already exists.", 3);
         }

      }
   }

   public void setDir(File dir) {
      this.dir = dir;
   }

   private boolean mkdirs(File f) {
      if (!f.mkdirs()) {
         try {
            Thread.sleep(10L);
            return f.mkdirs();
         } catch (InterruptedException var3) {
            return f.mkdirs();
         }
      } else {
         return true;
      }
   }
}
