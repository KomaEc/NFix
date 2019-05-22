package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class DifferentSelector extends MappingSelector {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private boolean ignoreFileTimes = true;
   private boolean ignoreContents = false;

   public void setIgnoreFileTimes(boolean ignoreFileTimes) {
      this.ignoreFileTimes = ignoreFileTimes;
   }

   public void setIgnoreContents(boolean ignoreContents) {
      this.ignoreContents = ignoreContents;
   }

   protected boolean selectionTest(File srcfile, File destfile) {
      if (srcfile.exists() != destfile.exists()) {
         return true;
      } else if (srcfile.length() != destfile.length()) {
         return true;
      } else {
         if (!this.ignoreFileTimes) {
            boolean sameDate = destfile.lastModified() >= srcfile.lastModified() - (long)this.granularity && destfile.lastModified() <= srcfile.lastModified() + (long)this.granularity;
            if (!sameDate) {
               return true;
            }
         }

         if (!this.ignoreContents) {
            try {
               return !FILE_UTILS.contentEquals(srcfile, destfile);
            } catch (IOException var4) {
               throw new BuildException("while comparing " + srcfile + " and " + destfile, var4);
            }
         } else {
            return false;
         }
      }
   }
}
