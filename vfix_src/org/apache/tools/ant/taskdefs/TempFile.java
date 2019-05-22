package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

public class TempFile extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private String property;
   private File destDir = null;
   private String prefix;
   private String suffix = "";
   private boolean deleteOnExit;

   public void setProperty(String property) {
      this.property = property;
   }

   public void setDestDir(File destDir) {
      this.destDir = destDir;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public void setDeleteOnExit(boolean deleteOnExit) {
      this.deleteOnExit = deleteOnExit;
   }

   public boolean isDeleteOnExit() {
      return this.deleteOnExit;
   }

   public void execute() throws BuildException {
      if (this.property != null && this.property.length() != 0) {
         if (this.destDir == null) {
            this.destDir = this.getProject().resolveFile(".");
         }

         File tfile = FILE_UTILS.createTempFile(this.prefix, this.suffix, this.destDir, this.deleteOnExit);
         this.getProject().setNewProperty(this.property, tfile.toString());
      } else {
         throw new BuildException("no property specified");
      }
   }
}
