package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

/** @deprecated */
public class Rename extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File src;
   private File dest;
   private boolean replace = true;

   public void setSrc(File src) {
      this.src = src;
   }

   public void setDest(File dest) {
      this.dest = dest;
   }

   public void setReplace(String replace) {
      this.replace = Project.toBoolean(replace);
   }

   public void execute() throws BuildException {
      this.log("DEPRECATED - The rename task is deprecated.  Use move instead.");
      if (this.dest == null) {
         throw new BuildException("dest attribute is required", this.getLocation());
      } else if (this.src == null) {
         throw new BuildException("src attribute is required", this.getLocation());
      } else if (!this.replace && this.dest.exists()) {
         throw new BuildException(this.dest + " already exists.");
      } else {
         try {
            FILE_UTILS.rename(this.src, this.dest);
         } catch (IOException var2) {
            throw new BuildException("Unable to rename " + this.src + " to " + this.dest, var2, this.getLocation());
         }
      }
   }
}
