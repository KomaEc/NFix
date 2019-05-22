package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Basename extends Task {
   private File file;
   private String property;
   private String suffix;

   public void setFile(File file) {
      this.file = file;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public void execute() throws BuildException {
      if (this.property == null) {
         throw new BuildException("property attribute required", this.getLocation());
      } else if (this.file == null) {
         throw new BuildException("file attribute required", this.getLocation());
      } else {
         String value = this.file.getName();
         if (this.suffix != null && value.endsWith(this.suffix)) {
            int pos = value.length() - this.suffix.length();
            if (pos > 0 && this.suffix.charAt(0) != '.' && value.charAt(pos - 1) == '.') {
               --pos;
            }

            value = value.substring(0, pos);
         }

         this.getProject().setNewProperty(this.property, value);
      }
   }
}
