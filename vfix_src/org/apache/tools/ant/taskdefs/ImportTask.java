package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

public class ImportTask extends Task {
   private String file;
   private boolean optional;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   public void setOptional(boolean optional) {
      this.optional = optional;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public void execute() {
      if (this.file == null) {
         throw new BuildException("import requires file attribute");
      } else if (this.getOwningTarget() != null && "".equals(this.getOwningTarget().getName())) {
         ProjectHelper helper = (ProjectHelper)this.getProject().getReference("ant.projectHelper");
         if (helper == null) {
            throw new BuildException("import requires support in ProjectHelper");
         } else {
            Vector importStack = helper.getImportStack();
            if (importStack.size() == 0) {
               throw new BuildException("import requires support in ProjectHelper");
            } else if (this.getLocation() != null && this.getLocation().getFileName() != null) {
               File buildFile = (new File(this.getLocation().getFileName())).getAbsoluteFile();
               File buildFileParent = new File(buildFile.getParent());
               File importedFile = FILE_UTILS.resolveFile(buildFileParent, this.file);
               this.getProject().log("Importing file " + importedFile + " from " + buildFile.getAbsolutePath(), 3);
               if (!importedFile.exists()) {
                  String message = "Cannot find " + this.file + " imported from " + buildFile.getAbsolutePath();
                  if (this.optional) {
                     this.getProject().log(message, 3);
                  } else {
                     throw new BuildException(message);
                  }
               } else if (importStack.contains(importedFile)) {
                  this.getProject().log("Skipped already imported file:\n   " + importedFile + "\n", 3);
               } else {
                  try {
                     helper.parse(this.getProject(), importedFile);
                  } catch (BuildException var7) {
                     throw ProjectHelper.addLocationToBuildException(var7, this.getLocation());
                  }
               }
            } else {
               throw new BuildException("Unable to get location of import task");
            }
         }
      } else {
         throw new BuildException("import only allowed as a top-level task");
      }
   }
}
