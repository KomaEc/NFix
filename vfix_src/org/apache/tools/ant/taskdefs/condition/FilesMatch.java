package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class FilesMatch implements Condition {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File file1;
   private File file2;
   private boolean textfile = false;

   public void setFile1(File file1) {
      this.file1 = file1;
   }

   public void setFile2(File file2) {
      this.file2 = file2;
   }

   public void setTextfile(boolean textfile) {
      this.textfile = textfile;
   }

   public boolean eval() throws BuildException {
      if (this.file1 != null && this.file2 != null) {
         boolean matches = false;

         try {
            matches = FILE_UTILS.contentEquals(this.file1, this.file2, this.textfile);
            return matches;
         } catch (IOException var3) {
            throw new BuildException("when comparing files: " + var3.getMessage(), var3);
         }
      } else {
         throw new BuildException("both file1 and file2 are required in filesmatch");
      }
   }
}
